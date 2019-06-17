package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.realmarket.propeler.model.Auth;
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
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  private final EmailService emailService;
  private final AuthService authService;

  @Value(value = "${cos.file_prefix.campaign_market_image}")
  private String companyFeaturedImage;

  @Value(value = "${frontend.service.url}")
  private String frontendServiceUrlPath;

  @Autowired
  public CampaignServiceImpl(
      CampaignRepository campaignRepository,
      CompanyService companyService,
      @Lazy CampaignTopicService campaignTopicService,
      CampaignStateService campaignStateService,
      ModelMapperBlankString modelMapperBlankString,
      CloudObjectStorageService cloudObjectStorageService,
      PlatformSettingsService platformSettingsService,
      OTPService otpService,
      EmailService emailService,
      AuthService authService) {
    this.campaignRepository = campaignRepository;
    this.companyService = companyService;
    this.campaignTopicService = campaignTopicService;
    this.campaignStateService = campaignStateService;
    this.modelMapperBlankString = modelMapperBlankString;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.platformSettingsService = platformSettingsService;
    this.otpService = otpService;
    this.emailService = emailService;
    this.authService = authService;
  }

  public Campaign findByUrlFriendlyNameOrThrowException(String urlFriendlyName) {
    return campaignRepository
        .findByUrlFriendlyNameAndDeletedFalse(urlFriendlyName)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_NOT_FOUND));
  }

  @Override
  public List<Campaign> findAllByCompany(Company company) {
    return campaignRepository.findAllByCompany(company);
  }

  @Transactional
  public CampaignResponseDto createCampaign(CampaignDto campaignDto) {

    Company company = companyService.findByIdOrThrowException(campaignDto.getCompanyId());

    throwIfNotCompanyOwner(company);
    throwIfCompanyHasActiveCampaign(company);
    throwIfCampaignNameExists(campaignDto.getUrlFriendlyName());

    Campaign campaign = new Campaign(campaignDto);
    campaign.setCompany(company);
    campaign.setCampaignState(campaignStateService.getCampaignState(CampaignStateName.INITIAL));
    validateCampaign(campaign);

    return new CampaignResponseDto(campaignRepository.save(campaign));
  }

  public CampaignResponseDto patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNotOwnerOrNotEditable(campaign);
    modelMapperBlankString.map(campaignPatchDto, campaign);
    validateCampaign(campaign);
    return new CampaignResponseDto(campaignRepository.save(campaign));
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

  public void throwIfNotActive(Campaign campaign) {
    if (!campaign.getCampaignState().getName().equals(CampaignStateName.ACTIVE)) {
      throw new BadRequestException(CAMPAIGN_IS_NOT_ACTIVE);
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
    // TODO: Temporary here. Remove this line when campaign could be set to active state.
    sendNewCampaignOpportunityEmail(campaign);
    campaignRepository.save(campaign);
  }

  @Override
  public Page<CampaignResponseDto> getPublicCampaigns(Pageable pageable, String filter) {

    if (filter.equalsIgnoreCase("all")) {
      return campaignRepository.findAllPublic(pageable).map(CampaignResponseDto::new);
    } else if (filter.equalsIgnoreCase("active") || filter.equalsIgnoreCase("post_campaign")) {
      return campaignRepository
          .findAllByCampaignState(pageable, campaignStateService.getCampaignState(filter))
          .map(CampaignResponseDto::new);
    }
    throw new BadRequestException(INVALID_REQUEST);
  }

  @Override
  public void sendNewCampaignOpportunityEmail(Campaign campaign) {
    CampaignTopicDto campaignTopicDto =
        campaignTopicService.getCampaignTopic(campaign.getUrlFriendlyName(), "OVERVIEW");
    CampaignEmailDto campaignEmailDto =
        new CampaignEmailDto(campaign, frontendServiceUrlPath, campaignTopicDto.getContent());
    List<String> emails =
        authService.findAllInvestors().stream()
            .map(auth -> auth.getPerson().getEmail())
            .collect(Collectors.toList());

    emailService.sendMailToUser(
        new MailContentHolder(
            emails,
            EEmailType.NEW_CAMPAIGN_OPPORTUNITY,
            Collections.unmodifiableMap(
                Stream.of(
                        new AbstractMap.SimpleEntry<>(EmailServiceImpl.CAMPAIGN, campaignEmailDto))
                    .collect(
                        Collectors.toMap(
                            AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)))));
  }

  @Override
  public void sendNewCampaignOpportunitiesEmail() {
    List<CampaignEmailDto> campaignEmailList =
        findActiveCampaigns().stream()
            .map(campaign -> new CampaignEmailDto(campaign, frontendServiceUrlPath, ""))
            .collect(Collectors.toList());

    List<String> emails =
        authService.findAllInvestors().stream()
            .map(auth -> auth.getPerson().getEmail())
            .collect(Collectors.toList());

    emailService.sendMailToUser(
        new MailContentHolder(
            emails,
            EEmailType.NEW_CAMPAIGN_OPPORTUNITIES,
            Collections.unmodifiableMap(
                Stream.of(
                        new AbstractMap.SimpleEntry<>(
                            EmailServiceImpl.CAMPAIGNS, campaignEmailList))
                    .collect(
                        Collectors.toMap(
                            AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)))));
  }

  @Transactional
  @Override
  public void increaseCollectedAmount(Campaign campaign, BigDecimal amountOfMoney) {
    campaign.setCollectedAmount(campaign.getCollectedAmount().add(amountOfMoney));
    campaignRepository.save(campaign);
  }

  @Transactional
  @Override
  public void decreaseCollectedAmount(Campaign campaign, BigDecimal amountOfMoney) {
    campaign.setCollectedAmount(campaign.getCollectedAmount().subtract(amountOfMoney));
    campaignRepository.save(campaign);
  }

  private List<Campaign> findActiveCampaigns() {
    return campaignRepository.findAllByCampaignState(
        campaignStateService.getCampaignState("ACTIVE"));
  }

  @Override
  public List<Campaign> findByCompany(Company company) {
    return campaignRepository.findByCompany(company);
  }

  @Override
  public List<CampaignResponseDto> getAllCampaignsForUser() {
    Auth user = AuthenticationUtil.getAuthentication().getAuth();
    switch (user.getUserRole().getName()) {
      case ROLE_ENTREPRENEUR:
        Company company = companyService.findMyCompany();
        return findByCompany(company).stream()
            .map(CampaignResponseDto::new)
            .collect(Collectors.toList());
      default:
        throw new ForbiddenOperationException(FORBIDDEN_OPERATION_EXCEPTION);
    }
  }

  @Override
  public BigDecimal convertMoneyToPercentageOfEquity(String campaignName, BigDecimal money) {
    if (money.compareTo(BigDecimal.valueOf(0)) < 0) {
      throw new BadRequestException(NEGATIVE_VALUE_EXCEPTION);
    }

    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNotActive(campaign);

    BigDecimal maxInvest =
        BigDecimal.valueOf(campaign.getFundingGoals())
            .multiply(campaign.getMaxEquityOffered().divide(campaign.getMinEquityOffered()));
    if (money.compareTo(campaign.getMinInvestment()) < 0) {
      throw new BadRequestException(INVESTMENT_MUST_BE_GREATER_THAN_CAMPAIGN_MIN_INVESTMENT);
    } else if (money.compareTo(maxInvest) > 0) {
      throw new BadRequestException(INVESTMENT_CAN_NOT_BE_GREATER_THAN_MAX_INVESTMENT);
    }

    return money
        .divide(BigDecimal.valueOf(campaign.getFundingGoals()))
        .multiply(campaign.getMinEquityOffered());
  }

  @Override
  public BigDecimal convertPercentageOfEquityToMoney(
      String campaignName, BigDecimal percentageOfEquity) {
    if (percentageOfEquity.compareTo(BigDecimal.valueOf(0)) < 0) {
      throw new BadRequestException(NEGATIVE_VALUE_EXCEPTION);
    }

    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNotActive(campaign);

    if (percentageOfEquity.compareTo(campaign.getMaxEquityOffered()) > 0) {
      throw new BadRequestException(INVESTMENT_CAN_NOT_BE_GREATER_THAN_CAMPAIGN_MAXIMUM_EQUITY);
    }

    return percentageOfEquity
        .divide(campaign.getMinEquityOffered())
        .multiply(BigDecimal.valueOf(campaign.getFundingGoals()));
  }
}
