package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignTopicService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.exception.ActiveCampaignAlreadyExistsException;
import io.realmarket.propeler.service.exception.CampaignNameAlreadyExistsException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.FileUtils;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Slf4j
@Service
public class CampaignServiceImpl implements CampaignService {

  private final CampaignRepository campaignRepository;
  private final CloudObjectStorageService cloudObjectStorageService;
  private final CompanyService companyService;
  private final CampaignTopicService campaignTopicService;
  private final ModelMapperBlankString modelMapperBlankString;

  @Value(value = "${cos.file_prefix.campaign_market_image}")
  private String companyFeaturedImage;

  @Autowired
  public CampaignServiceImpl(
      CampaignRepository campaignRepository,
      CompanyService companyService,
      @Lazy CampaignTopicService campaignTopicService,
      ModelMapperBlankString modelMapperBlankString,
      CloudObjectStorageService cloudObjectStorageService) {
    this.campaignRepository = campaignRepository;
    this.companyService = companyService;
    this.campaignTopicService = campaignTopicService;
    this.modelMapperBlankString = modelMapperBlankString;
    this.cloudObjectStorageService = cloudObjectStorageService;
  }

  @Override
  public Campaign findByUrlFriendlyNameOrThrowException(String urlFriendlyName) {
    return campaignRepository
        .findByUrlFriendlyName(urlFriendlyName)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_NOT_FOUND));
  }

  @Transactional
  public void createCampaign(CampaignDto campaignDto) {
    campaignRepository
        .findByCompanyIdAndActiveTrue(campaignDto.getCompanyId())
        .ifPresent(
            c -> {
              throw new ActiveCampaignAlreadyExistsException();
            });

    if (campaignRepository.findByUrlFriendlyName(campaignDto.getUrlFriendlyName()).isPresent()) {
      log.error("Campaign with the provided name '{}' already exists!", campaignDto.getName());
      throw new CampaignNameAlreadyExistsException(ExceptionMessages.CAMPAIGN_NAME_ALREADY_EXISTS);
    }

    Company company = companyService.findByIdOrThrowException(campaignDto.getCompanyId());

    if (!AuthenticationUtil.isAuthenticatedUserId(company.getAuth().getId())) {
      throw new AccessDeniedException(ExceptionMessages.NOT_COMPANY_OWNER);
    }

    Campaign campaign = new Campaign(campaignDto);
    campaign.setActive(true);
    campaign.setCompany(company);
    campaignRepository.save(campaign);

    log.info("Campaign with name '{}' saved successfully.", campaignDto.getUrlFriendlyName());
  }

  public CampaignDto patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNoAccess(campaign);
    modelMapperBlankString.map(campaignPatchDto, campaign);
    return new CampaignDto(campaignRepository.save(campaign));
  }

  public Campaign getActiveCampaignForCompany() {
    final Company company =
        companyService.findByAuthIdOrThrowException(
            AuthenticationUtil.getAuthentication().getAuth().getId());
    return campaignRepository
        .findByCompanyIdAndActiveTrue(company.getId())
        .orElseThrow(() -> new EntityNotFoundException(NO_ACTIVE_CAMPAIGN));
  }

  public CampaignDto getActiveCampaignDto() {
    Campaign campaign = getActiveCampaignForCompany();
    CampaignDto campaignDto = new CampaignDto(campaign);
    campaignDto.setTopicStatus(campaignTopicService.getTopicStatus(campaign));
    return campaignDto;
  }

  public Campaign findByIdOrThrowException(Long id) {
    return campaignRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException("Campaign with provided id does not exist."));
  }

  public void throwIfNoAccess(Campaign campaign) {
    if (!campaign
        .getCompany()
        .getAuth()
        .getId()
        .equals(AuthenticationUtil.getAuthentication().getAuth().getId())) {
      throw new ForbiddenOperationException(USER_IS_NOT_OWNER_OF_CAMPAIGN);
    }
  }

  @Override
  @Transactional
  public void uploadMarketImage(String campaignName, MultipartFile logo) {
    log.info("Market image upload requested");
    String extension = FileUtils.getExtensionOrThrowException(logo);
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNoAccess(campaign);
    String url =
        String.join("", companyFeaturedImage, campaign.getUrlFriendlyName(), ".", extension);
    cloudObjectStorageService.uploadAndReplace(campaign.getMarketImageUrl(), url, logo);
    campaign.setMarketImageUrl(url);
    campaignRepository.save(campaign);
  }

  @Override
  public FileDto downloadMarketImage(String campaignName) {
    return cloudObjectStorageService.downloadFileDto(
        findByUrlFriendlyNameOrThrowException(campaignName).getMarketImageUrl());
  }

  @Override
  @Transactional
  public void deleteMarketImage(String campaignName) {
    log.info("Delete campaign[{}] market image requested", campaignName);
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNoAccess(campaign);
    cloudObjectStorageService.delete(campaign.getMarketImageUrl());
    campaign.setMarketImageUrl(null);
    campaignRepository.save(campaign);
  }
}
