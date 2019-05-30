package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Campaign;
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

    Company company = companyService.findByIdOrThrowException(campaignDto.getCompanyId());

    throwIfNotCompanyOwner(company);
    throwIfCompanyHasActiveCampaign(company);
    throwIfCampaignNameExists(campaignDto.getUrlFriendlyName());

    Campaign campaign = new Campaign(campaignDto);
    campaign.setCompany(company);
    campaign.setCampaignState(campaignStateService.getCampaignState(CampaignStateName.INITIAL));
    validateCampaign(campaign);
    campaignRepository.save(campaign);

    log.info("Campaign with name '{}' saved successfully.", campaignDto.getUrlFriendlyName());
  }

  public CampaignDto patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNotOwnerOrNotEditable(campaign);
    modelMapperBlankString.map(campaignPatchDto, campaign);
    validateCampaign(campaign);
    return new CampaignDto(campaignRepository.save(campaign));
  }

  public Campaign getActiveCampaignForCompany() {
    final Company company =
        companyService.findByAuthIdOrThrowException(
            AuthenticationUtil.getAuthentication().getAuth().getId());
    return campaignRepository
        .findExistingByCompany(company)
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
    campaign.setCampaignState(campaignStateService.getCampaignState(CampaignStateName.DELETED));
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

  public void throwIfNotOwnerOrNotEditable(Campaign campaign) {
    throwIfNoAccess(campaign);
    throwIfNotEditable(campaign);
  }

  public void throwIfNoAccess(Campaign campaign) {
    if (!isOwner(campaign)) {
      throw new ForbiddenOperationException(USER_IS_NOT_OWNER_OF_CAMPAIGN);
    }
  }

  public void throwIfNotEditable(Campaign campaign) {
    switch (campaign.getCampaignState().getName()) {
      case INITIAL:
      case REVIEW_READY:
        return;
      default:
        throw new ForbiddenOperationException(CAMPAIGN_NOT_EDITABLE);
    }
  }

  @Override
  @Transactional
  public void uploadMarketImage(String campaignName, MultipartFile logo) {
    log.info("Market image upload requested");
    String extension = FileUtils.getExtensionOrThrowException(logo);
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNotOwnerOrNotEditable(campaign);
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
    throwIfNotOwnerOrNotEditable(campaign);
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

  private void throwIfNotCompanyOwner(Company company) {
    if (!AuthenticationUtil.isAuthenticatedUserId(company.getAuth().getId())) {
      throw new AccessDeniedException(ExceptionMessages.NOT_COMPANY_OWNER);
    }
  }

  private void throwIfCompanyHasActiveCampaign(Company company) {
    campaignRepository
        .findExistingByCompany(company)
        .ifPresent(
            c -> {
              throw new ActiveCampaignAlreadyExistsException();
            });
  }

  private void throwIfCampaignNameExists(String campaignName) {
    if (campaignRepository.findByUrlFriendlyNameAndDeletedFalse(campaignName).isPresent()) {
      log.error("Campaign with the provided name '{}' already exists!", campaignName);
      throw new CampaignNameAlreadyExistsException(ExceptionMessages.CAMPAIGN_NAME_ALREADY_EXISTS);
    }
  }

  @Override
  @Transactional
  public void requestReviewForCampaign(String campaignName) {
    Campaign campaign = getCampaignByUrlFriendlyName(campaignName);
    if (!campaignStateService.changeState(
        campaign,
        campaignStateService.getCampaignState(CampaignStateName.REVIEW_READY.toString()),
        isOwner(campaign))) {
      throw new ForbiddenOperationException(FORBIDDEN_OPERATION_EXCEPTION);
    }
    campaignRepository.save(campaign);
  }
}
