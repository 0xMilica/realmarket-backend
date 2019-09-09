package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.campaign.CampaignSubmissionForReviewDto;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.exception.ActiveCampaignAlreadyExistsException;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.CampaignNameAlreadyExistsException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.AuthUtil;
import io.realmarket.propeler.service.util.FileUtils;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.service.util.email.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
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
  private final AuditService auditService;
  private final BlockchainMessageProducer blockchainMessageProducer;
  private final InvestmentService investmentService;
  private final AuthUtil authUtil;

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
      AuthService authService,
      AuditService auditService,
      BlockchainMessageProducer blockchainMessageProducer,
      @Lazy InvestmentService investmentService,
      @Lazy AuthUtil authUtil) {
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
    this.auditService = auditService;
    this.blockchainMessageProducer = blockchainMessageProducer;
    this.investmentService = investmentService;
    this.authUtil = authUtil;
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
    campaign.setCurrency(platformSettingsService.getPlatformCurrency().getCode());
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

  public Campaign getActiveCampaign() {
    final Company company =
        companyService.findByAuthIdOrThrowException(
            AuthenticationUtil.getAuthentication().getAuth().getId());
    return campaignRepository
        .findByCompanyAndActiveTrue(company)
        .orElseThrow(() -> new EntityNotFoundException(NO_ACTIVE_CAMPAIGN));
  }

  public CampaignResponseDto getCurrentCampaignDto() {
    final Company company =
        companyService.findByAuthIdOrThrowException(
            AuthenticationUtil.getAuthentication().getAuth().getId());
    Campaign campaign =
        campaignRepository
            .findExistingByCompany(company)
            .orElseThrow(() -> new EntityNotFoundException(NO_ACTIVE_CAMPAIGN));
    CampaignResponseDto campaignDto = new CampaignResponseDto(campaign);
    campaignDto.setTopicStatus(campaignTopicService.getTopicStatus(campaign));
    return campaignDto;
  }

  public CampaignResponseDto getCampaignDtoByUrlFriendlyName(String name) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(name);
    CampaignResponseDto campaignDto;
    throwIfNoAccess(campaign);
    if (campaign.getCampaignState().getName().equals(CampaignStateName.AUDIT)) {
      campaignDto = getAuditCampaign(campaign);
    } else {
      campaignDto = new CampaignResponseDto(campaign);
    }
    campaignDto.setTopicStatus(campaignTopicService.getTopicStatus(campaign));
    return campaignDto;
  }

  private CampaignResponseDto getAuditCampaign(Campaign campaign) {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    Audit audit = auditService.findPendingAuditByCampaignOrThrowException(campaign);
    if (!audit.getAuditor().getId().equals(auth.getId())) {
      throw new BadRequestException(USER_IS_NOT_AUDITOR_OF_CAMPAIGN);
    }
    return new CampaignResponseDto(campaign, audit);
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

  public void throwIfNotOwner(Campaign campaign) {
    if (!isOwner(campaign)) {
      throw new ForbiddenOperationException(USER_IS_NOT_OWNER_OF_CAMPAIGN);
    }
  }

  public void throwIfNoAccess(Campaign campaign) {
    authUtil.setAuthentication();
    Auth auth = AuthenticationUtil.getAuthOrReturnNull();
    if (auth == null) {
      if (!campaign.getCampaignState().getName().equals(CampaignStateName.ACTIVE)
              && !campaign.getCampaignState().getName().equals(CampaignStateName.SUCCESSFUL)
              && !campaign.getCampaignState().getName().equals(CampaignStateName.UNSUCCESSFUL)) {
        throw new EntityNotFoundException();
      }
    } else {
      switch (auth.getUserRole().getName()) {
        case ROLE_ADMIN:
          break;
        case ROLE_ENTREPRENEUR:
          if (isOwner(campaign)) {
            break;
          }
        default:
          if (!campaign.getCampaignState().getName().equals(CampaignStateName.ACTIVE)
                  && !campaign.getCampaignState().getName().equals(CampaignStateName.SUCCESSFUL)
                  && !campaign.getCampaignState().getName().equals(CampaignStateName.UNSUCCESSFUL)) {
            throw new ForbiddenOperationException(USER_IS_NOT_OWNER_OF_CAMPAIGN);
          }
          break;
      }
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
                .compareTo(platformSettingsService.getPlatformMinimumInvestment())
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
    throwIfNotOwner(campaign);

    campaign = changeCampaignStateOrThrow(campaign, CampaignStateName.REVIEW_READY);

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.CAMPAIGN_SUBMISSION_FOR_REVIEW,
        new CampaignSubmissionForReviewDto(campaign),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());
  }

  @Override
  @Transactional
  public Campaign launchCampaign(String campaignName) {
    Campaign campaign = getCampaignByUrlFriendlyName(campaignName);
    campaign = changeCampaignStateOrThrow(campaign, CampaignStateName.ACTIVE);
    sendNewCampaignOpportunityEmail(campaign);
    return campaign;
  }

  @Override
  @Transactional
  public Campaign closeCampaign(String campaignName, CampaignClosingReasonDto campaignClosingReasonDto) {
    Campaign campaign = getCampaignByUrlFriendlyName(campaignName);
    throwIfNotActive(campaign);
    campaign.setClosingReason(campaignClosingReasonDto.getClosingReason());

    if (campaignClosingReasonDto.isSuccessful()) {
      campaign = changeCampaignStateOrThrow(campaign, CampaignStateName.SUCCESSFUL);
    }else{
      campaign = changeCampaignStateOrThrow(campaign, CampaignStateName.UNSUCCESSFUL);
    }

    return campaign;
  }

  @Override
  public Campaign changeCampaignStateOrThrow(
      Campaign campaign, CampaignStateName followingCampaignState) {
    campaignStateService.changeStateOrThrow(campaign, followingCampaignState);
    return campaignRepository.save(campaign);
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
  public Page<CampaignResponseDto> getCampaignsByState(Pageable pageable, String state) {
    if (state != null && state.equalsIgnoreCase("audit")) {
      Auth auth = AuthenticationUtil.getAuthentication().getAuth();
      return campaignRepository.findAuditCampaigns(auth, pageable).map(CampaignResponseDto::new);
    }
    return campaignRepository
        .findAllByCampaignState(
            pageable, (state == null) ? null : campaignStateService.getCampaignState(state))
        .map(CampaignResponseDto::new);
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

    emailService.sendEmailToUser(
        EmailType.NEW_CAMPAIGN_OPPORTUNITY,
        emails,
        Stream.of(new SimpleEntry<>(Parameters.CAMPAIGN, campaignEmailDto))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
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

    emailService.sendEmailToUser(
        EmailType.NEW_CAMPAIGN_OPPORTUNITIES,
        emails,
        Stream.of(new SimpleEntry<>(Parameters.CAMPAIGNS, campaignEmailList))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
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

  private BigDecimal getAbsoluteMaximumInvestmentAmount(Campaign campaign) {
    return BigDecimal.valueOf(campaign.getFundingGoals())
        .multiply(
            campaign
                .getMaxEquityOffered()
                .divide(campaign.getMinEquityOffered(), MathContext.DECIMAL128));
  }

  @Override
  public BigDecimal getMaximumInvestableAmount(Campaign campaign) {
    return getAbsoluteMaximumInvestmentAmount(campaign).subtract(campaign.getCollectedAmount());
  }

  @Override
  public BigDecimal getMaximumAcquirableEquity(Campaign campaign) {
    return campaign
        .getMaxEquityOffered()
        .multiply(
            getMaximumInvestableAmount(campaign)
                .divide(getAbsoluteMaximumInvestmentAmount(campaign), MathContext.DECIMAL128));
  }

  @Override
  public BigDecimal convertMoneyToPercentageOfEquity(String campaignName, BigDecimal money) {
    if (money.compareTo(BigDecimal.valueOf(0)) < 0) {
      throw new BadRequestException(NEGATIVE_VALUE_EXCEPTION);
    }

    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNotActive(campaign);

    BigDecimal maxInvest = getMaximumInvestableAmount(campaign);

    if (money.compareTo(campaign.getMinInvestment()) < 0) {
      throw new BadRequestException(INVESTMENT_MUST_BE_GREATER_THAN_CAMPAIGN_MIN_INVESTMENT);
    } else if (money.compareTo(maxInvest) > 0) {
      throw new BadRequestException(INVESTMENT_CAN_NOT_BE_GREATER_THAN_MAX_INVESTMENT);
    }

    return money
        .divide(BigDecimal.valueOf(campaign.getFundingGoals()), MathContext.DECIMAL128)
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

    BigDecimal acquirableEquity = getMaximumAcquirableEquity(campaign);

    if (percentageOfEquity.compareTo(acquirableEquity) > 0) {
      throw new BadRequestException(
          INVESTMENT_CAN_NOT_BE_GREATER_THAN_CAMPAIGN_MAXIMUM_EQUITY_AVAILABLE);
    }

    return percentageOfEquity
        .divide(campaign.getMinEquityOffered(), MathContext.DECIMAL128)
        .multiply(BigDecimal.valueOf(campaign.getFundingGoals()));
  }

  @Override
  public BigDecimal getAvailableInvestableAmount(String campaignName) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    return getMaximumInvestableAmount(campaign);
  }

  @Override
  public BigDecimal getAvailableEquity(String campaignName) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    return getMaximumAcquirableEquity(campaign);
  }

  @Override
  public AvailableInvestmentDto getAvailableInvestment(String campaignName) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    return AvailableInvestmentDto.builder()
        .amount(getMaximumInvestableAmount(campaign))
        .equity(getMaximumAcquirableEquity(campaign))
        .build();
  }

  @Override
  public Page<CampaignWithInvestmentsWithPersonResponseDto> getCampaignsByStateWithInvestments(
      Pageable pageable, String state) {
    if (state.equalsIgnoreCase("all")) {
      return campaignRepository
          .findAllByCompanyViewable(pageable, companyService.findMyCompany())
          .map(
              c ->
                  new CampaignWithInvestmentsWithPersonResponseDto(
                      c, investmentService.findAllByCampaignWithInvestors(c)));
    }
    else if (state.equalsIgnoreCase("completed")) {
      if (AuthenticationUtil.hasUserAdminRole()) {
        return campaignRepository
                .findAllCompletedCampaigns(pageable)
                .map(
                        c ->
                                new CampaignWithInvestmentsWithPersonResponseDto(
                                        (c), investmentService.findAllByCampaignWithInvestors(c)));
      }
    }
    else if (!state.equalsIgnoreCase("deleted")) {
      CampaignState campaignState = campaignStateService.getCampaignState(state);
      if (AuthenticationUtil.hasUserAdminRole()) {
        return campaignRepository
            .findAllByCampaignState(
                pageable, campaignStateService.getCampaignState(campaignState.getName().toString()))
            .map(
                c ->
                    new CampaignWithInvestmentsWithPersonResponseDto(
                        c, investmentService.findAllByCampaignWithInvestors(c)));
      } else {
        return campaignRepository
            .findAllByCampaignStateAndCompany(
                pageable,
                campaignStateService.getCampaignState(campaignState.getName().toString()),
                companyService.findMyCompany())
            .map(
                c ->
                    new CampaignWithInvestmentsWithPersonResponseDto(
                        c, investmentService.findAllByCampaignWithInvestors(c)));
      }
    }
    throw new BadRequestException(INVALID_REQUEST);
  }
}
