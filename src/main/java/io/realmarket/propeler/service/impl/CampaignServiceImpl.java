package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.ActiveCampaignAlreadyExistsException;
import io.realmarket.propeler.service.exception.BadRequestException;
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
  private final CampaignStateService campaignStateService;
  private final ModelMapperBlankString modelMapperBlankString;
  private final PlatformSettingsService platformSettingsService;
  private final OTPService otpService;

  @Value(value = "${cos.file_prefix.campaign_market_image}")
  private String companyFeaturedImage;

  @Autowired
  public CampaignServiceImpl(
      CampaignRepository campaignRepository,
      CompanyService companyService,
      @Lazy CampaignTopicService campaignTopicService,
      CampaignStateService campaignStateService,
      ModelMapperBlankString modelMapperBlankString,
      CloudObjectStorageService cloudObjectStorageService,
      PlatformSettingsService platformSettingsService,
      OTPService otpService) {
    this.campaignRepository = campaignRepository;
    this.companyService = companyService;
    this.campaignTopicService = campaignTopicService;
    this.campaignStateService = campaignStateService;
    this.modelMapperBlankString = modelMapperBlankString;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.platformSettingsService = platformSettingsService;
    this.otpService = otpService;
  }

  public Campaign findByUrlFriendlyNameOrThrowException(String urlFriendlyName) {
    return campaignRepository
        .findByUrlFriendlyNameAndDeletedFalse(urlFriendlyName)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_NOT_FOUND));
  }

  @Transactional
  public void createCampaign(CampaignDto campaignDto) {
    campaignRepository
        .findByCompanyIdAndActiveTrueAndDeletedFalse(campaignDto.getCompanyId())
        .ifPresent(
            c -> {
              throw new ActiveCampaignAlreadyExistsException();
            });

    if (campaignRepository
        .findByUrlFriendlyNameAndDeletedFalse(campaignDto.getUrlFriendlyName())
        .isPresent()) {
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
    validateCampaign(campaign);
    campaignRepository.save(campaign);

    log.info("Campaign with name '{}' saved successfully.", campaignDto.getUrlFriendlyName());
  }

  public CampaignDto patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNoAccess(campaign);
    modelMapperBlankString.map(campaignPatchDto, campaign);
    validateCampaign(campaign);
    return new CampaignDto(campaignRepository.save(campaign));
  }

  public Campaign getActiveCampaignForCompany() {
    final Company company =
        companyService.findByAuthIdOrThrowException(
            AuthenticationUtil.getAuthentication().getAuth().getId());
    return campaignRepository
        .findByCompanyIdAndActiveTrueAndDeletedFalse(company.getId())
        .orElseThrow(() -> new EntityNotFoundException(NO_ACTIVE_CAMPAIGN));
  }

  public CampaignDto getActiveCampaignDto() {
    Campaign campaign = getActiveCampaignForCompany();
    CampaignDto campaignDto = new CampaignDto(campaign);
    campaignDto.setTopicStatus(campaignTopicService.getTopicStatus(campaign));
    return campaignDto;
  }

  public CampaignDto getCampaignDtoByUrlFriendlyName(String name) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(name);
    CampaignDto campaignDto = new CampaignDto(campaign);
    campaignDto.setTopicStatus(campaignTopicService.getTopicStatus(campaign));
    return campaignDto;
  }

  public Campaign getCampaignByUrlFriendlyName(String name) {
    return findByUrlFriendlyNameOrThrowException(name);
  }

  @Transactional
  public void delete(String campaignName, TwoFADto twoFADto) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    if (!(otpService.validate(AuthenticationUtil.getAuthentication().getAuth(), twoFADto))) {
      throw new AccessDeniedException(INVALID_TOTP_CODE_PROVIDED);
    }
    throwIfNoAccess(campaign);
    campaign.setDeleted(true);
    campaignRepository.save(campaign);
  }

  public Campaign findByIdOrThrowException(Long id) {
    return campaignRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException("Campaign with provided id does not exist."));
  }

  public boolean isOwner(Campaign campaign) {
    return campaign
        .getCompany()
        .getAuth()
        .getId()
        .equals(AuthenticationUtil.getAuthentication().getAuth().getId());
  }

  public void throwIfNoAccess(Campaign campaign) {
    if (!isOwner(campaign)) {
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

  private void validateCampaign(Campaign campaign) {
    if (campaign.getMinInvestment() == null
        || campaign
                .getMinInvestment()
                .compareTo(
                    platformSettingsService.getCurrentPlatformSettings().getPlatformMinInvestment())
            < 0) {
      throw new BadRequestException(INVESTMENT_MUST_BE_GREATER_THAN_PLATFORM_MIN);
    }
  }

  @Override
  @Transactional
  public void requestReviewForCampaign(String campaignName) {
    Campaign campaign = getCampaignByUrlFriendlyName(campaignName);
    if (!campaignStateService.changeState(
        campaign,
        campaignStateService.getCampaignStateByName(CampaignStateName.REVIEW_READY.toString()),
        isOwner(campaign))) {
      throw new ForbiddenOperationException(FORBIDDEN_OPERATION_EXCEPTION);
    }
    campaignRepository.save(campaign);
  }
}
