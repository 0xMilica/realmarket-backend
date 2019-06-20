package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignTopicDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignTopic;
import io.realmarket.propeler.model.CampaignTopicType;
import io.realmarket.propeler.repository.CampaignTopicRepository;
import io.realmarket.propeler.repository.CampaignTopicTypeRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignTopicImageService;
import io.realmarket.propeler.service.CampaignTopicService;
import io.realmarket.propeler.service.exception.CampaignTopicTypeNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.CAMPAIGN_TOPIC_DOES_NOT_EXIST;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.CAMPAIGN_TOPIC_TYPE_DOES_NOT_EXIST;

@Service
public class CampaignTopicServiceImpl implements CampaignTopicService {

  private final CampaignTopicTypeRepository campaignTopicTypeRepository;
  private final CampaignTopicRepository campaignTopicRepository;

  private final CampaignService campaignService;
  private final CampaignTopicImageService campaignTopicImageService;

  @Autowired
  public CampaignTopicServiceImpl(
      CampaignTopicTypeRepository campaignTopicTypeRepository,
      CampaignTopicRepository campaignTopicRepository,
      CampaignService campaignService,
      CampaignTopicImageService campaignTopicTypeImageService) {
    this.campaignTopicTypeRepository = campaignTopicTypeRepository;
    this.campaignTopicRepository = campaignTopicRepository;
    this.campaignService = campaignService;
    this.campaignTopicImageService = campaignTopicTypeImageService;
  }

  public void createCampaignTopic(
      String campaignName, String topicType, CampaignTopicDto campaignTopicDto) {

    CampaignTopicType campaignTopicType = findByTopicTypeOrThrowException(topicType);
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNotOwnerOrNotEditable(campaign);

    campaignTopicRepository.save(
        CampaignTopic.builder()
            .campaign(campaign)
            .campaignTopicType(campaignTopicType)
            .content(campaignTopicDto.getContent())
            .build());
  }

  public CampaignTopicDto getCampaignTopic(String campaignName, String topicType) {
    final CampaignTopicType campaignTopicType =
        campaignTopicTypeRepository
            .findByNameIgnoreCase(topicType)
            .orElseThrow(
                () -> new CampaignTopicTypeNotExistException(CAMPAIGN_TOPIC_TYPE_DOES_NOT_EXIST));
    final Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNoAccess(campaign);

    return new CampaignTopicDto(
        findByCampaignAndCampaignTopicTypeOrThrowException(campaign, campaignTopicType)
            .getContent());
  }

  public CampaignTopicType findByTopicTypeOrThrowException(String topicType) {
    return campaignTopicTypeRepository
        .findByNameIgnoreCase(topicType)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_TOPIC_TYPE_DOES_NOT_EXIST));
  }

  public CampaignTopic findByCampaignAndCampaignTopicTypeOrThrowException(
      Campaign campaign, CampaignTopicType campaignTopicType) {
    return campaignTopicRepository
        .findByCampaignAndCampaignTopicType(campaign, campaignTopicType)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_TOPIC_DOES_NOT_EXIST));
  }

  @Override
  public Map<String, Boolean> getTopicStatus(Campaign campaign) {
    Map<String, Boolean> topicStatus = new HashMap<>();

    Map<Long, CampaignTopicType> allTopicTypes =
        campaignTopicTypeRepository.findAll().stream()
            .collect(Collectors.toMap(CampaignTopicType::getId, item -> item));
    allTopicTypes.forEach((k, v) -> topicStatus.put(v.getName().toLowerCase(), false));
    campaignTopicRepository
        .selectAllTopicsByCampaign(campaign)
        .forEach(topicType -> topicStatus.replace(topicType.getName().toLowerCase(), true));
    return topicStatus;
  }

  public void updateCampaignTopic(
      String campaignName, String topicType, CampaignTopicDto campaignTopicDto) {
    CampaignTopicType campaignTopicType = findByTopicTypeOrThrowException(topicType);
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNotOwnerOrNotEditable(campaign);

    CampaignTopic campaignTopic =
        findByCampaignAndCampaignTopicTypeOrThrowException(campaign, campaignTopicType);
    campaignTopic.setContent(campaignTopicDto.getContent());
    campaignTopic = campaignTopicRepository.save(campaignTopic);

    campaignTopicImageService.removeObsoleteImages(campaignTopic);
  }
}
