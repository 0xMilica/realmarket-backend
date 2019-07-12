package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FilenameDto;
import io.realmarket.propeler.model.CampaignUpdate;
import io.realmarket.propeler.model.CampaignUpdateImage;
import io.realmarket.propeler.repository.CampaignUpdateImageRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignUpdateImageService;
import io.realmarket.propeler.service.CampaignUpdateService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CampaignUpdateImageServiceImpl implements CampaignUpdateImageService {

    private final CampaignUpdateImageRepository campaignUpdateImageRepository;
    private final CampaignService campaignService;
    private final CampaignUpdateService campaignUpdateService;
    private final CloudObjectStorageService cloudObjectStorageService;

    @Autowired
    public CampaignUpdateImageServiceImpl(
            CampaignUpdateImageRepository campaignUpdateImageRepository,
            CampaignService campaignService,
            @Lazy CampaignUpdateService campaignUpdateService,
            CloudObjectStorageService cloudObjectStorageService) {
        this.campaignUpdateImageRepository = campaignUpdateImageRepository;
        this.campaignService = campaignService;
        this.campaignUpdateService = campaignUpdateService;
        this.cloudObjectStorageService = cloudObjectStorageService;
    }

    @Override
    @Async
    @Transactional
    public void removeObsoleteImages(CampaignUpdate campaignUpdate) {
        campaignUpdateImageRepository
                .findByCampaignUpdate(campaignUpdate)
                .forEach(
                        campaignUpdateImage -> {
                            if (!campaignUpdate.getContent().contains(campaignUpdateImage.getUrl())) {
                                cloudObjectStorageService.delete(campaignUpdateImage.getUrl());
                                campaignUpdateImageRepository.delete(campaignUpdateImage);
                            }
                        });
    }

    @Override
    @Transactional
    public FilenameDto uploadImage(Long campaignUpdateId, MultipartFile image) {
        CampaignUpdate campaignUpdate =
                campaignUpdateService.findByIdOrThrowException(campaignUpdateId);

        campaignService.throwIfNotOwner(campaignUpdate.getCampaign());
        campaignService.throwIfNotActive(campaignUpdate.getCampaign());

        String url = cloudObjectStorageService.uploadPublic(image);
        campaignUpdateImageRepository.save(
                CampaignUpdateImage.builder().url(url).campaignUpdate(campaignUpdate).build());

        return new FilenameDto(String.join("", FileServiceImpl.PUBLIC_FILE_ENDPOINT, url));
    }

    @Override
    @Async
    @Transactional
    public void removeImages(CampaignUpdate campaignUpdate) {
        campaignUpdateImageRepository
                .findByCampaignUpdate(campaignUpdate)
                .forEach(
                        campaignUpdateImage -> {
                            cloudObjectStorageService.delete(campaignUpdateImage.getUrl());
                            campaignUpdateImageRepository.delete(campaignUpdateImage);
                        });
    }
}
