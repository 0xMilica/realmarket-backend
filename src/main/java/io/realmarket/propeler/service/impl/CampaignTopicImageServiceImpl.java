package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FilenameDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignTopic;
import io.realmarket.propeler.model.CampaignTopicImage;
import io.realmarket.propeler.model.CampaignTopicType;
import io.realmarket.propeler.repository.CampaignTopicImageRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignTopicImageService;
import io.realmarket.propeler.service.CampaignTopicService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CampaignTopicImageServiceImpl implements CampaignTopicImageService {

  private final CampaignTopicImageRepository campaignTopicImageRepository;
  private final CampaignService campaignService;
  private final CampaignTopicService campaignTopicService;
  private CloudObjectStorageService cloudObjectStorageService;

  @Autowired
  public CampaignTopicImageServiceImpl(
      CampaignService campaignService,
      @Lazy CampaignTopicService campaignTopicService,
      CampaignTopicImageRepository campaignTopicImageRepository,
      CloudObjectStorageService cloudObjectStorageService) {
    this.campaignService = campaignService;
    this.campaignTopicService = campaignTopicService;
    this.campaignTopicImageRepository = campaignTopicImageRepository;
    this.cloudObjectStorageService = cloudObjectStorageService;
  }

  @Async
  @Transactional
  public void removeObsoleteImages(CampaignTopic campaignTopic) {
    campaignTopicImageRepository
        .findByCampaignTopic(campaignTopic)
        .forEach(
            topicImage -> {
              if (!campaignTopic.getContent().contains(topicImage.getUrl())) {
                cloudObjectStorageService.delete(topicImage.getUrl());
                campaignTopicImageRepository.delete(topicImage);
              }
            });
  }

  @Transactional
  public FilenameDto uploadImage(String campaignName, String topicType, MultipartFile image) {
    CampaignTopicType campaignTopicType =
        campaignTopicService.findByTopicTypeOrThrowException(topicType);
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNotOwnerOrNotEditable(campaign);

    CampaignTopic campaignTopic =
        campaignTopicService.findByCampaignAndCampaignTopicTypeOrThrowException(
            campaign, campaignTopicType);

    String url = cloudObjectStorageService.uploadPublic(image);
    campaignTopicImageRepository.save(
        CampaignTopicImage.builder().url(url).campaignTopic(campaignTopic).build());

    return new FilenameDto(String.join("", FileServiceImpl.PUBLIC_FILE_ENDPOING, url));
  }
}
