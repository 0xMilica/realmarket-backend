package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.api.dto.CampaignResponseDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.ActiveCampaignAlreadyExistsException;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.CampaignNameAlreadyExistsException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.*;
import static io.realmarket.propeler.util.CampaignTopicUtil.TEST_CAMPAIGN_TOPIC_DTO;
import static io.realmarket.propeler.util.CampaignUtils.*;
import static io.realmarket.propeler.util.CompanyUtils.TEST_FEATURED_IMAGE_URL;
import static io.realmarket.propeler.util.CompanyUtils.getCompanyMocked;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignServiceImpl.class)
public class CampaignServiceImplTest {
  @Mock CampaignRepository campaignRepository;

  @Mock CompanyService companyService;

  @Mock private ModelMapperBlankString modelMapperBlankString;

  @Mock private CloudObjectStorageService cloudObjectStorageService;

  @Mock private CampaignTopicService campaignTopicService;

  @Mock private PlatformSettingsService platformSettingsService;

  @Mock private OTPService otpService;

  @Mock private CampaignStateService campaignStateService;

  @Mock private AuthService authService;

  @Mock private AuditService auditService;

  @Mock private EmailService emailService;

  @InjectMocks private CampaignServiceImpl campaignServiceImpl;

  @Before
  public void createAuthContext() {
    mockRequestAndContext();
    ReflectionTestUtils.setField(
        campaignServiceImpl, "frontendServiceUrlPath", "http://localhost:3000/propeler");
  }

  @Test
  public void findByUrlFriendlyNameOrThrowException_Should_ReturnCampaign_IfUserExists() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_CAMPAIGN));

    Campaign retVal =
        campaignServiceImpl.findByUrlFriendlyNameOrThrowException(TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_CAMPAIGN, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FindByUrlFriendlyNameOrThrowException_Should_ThrowException_IfUserNotExists() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.empty());

    campaignServiceImpl.findByUrlFriendlyNameOrThrowException(TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void PatchCampaign_Should_CallModelMapper() {
    AuthUtils.mockRequestAndContextEntrepreneur();
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();
    CampaignPatchDto campaignPatchDto = CampaignUtils.TEST_CAMPAIGN_PATCH_DTO_FUNDING_GOALS();
    PowerMockito.when(
            campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(testCampaign));
    PowerMockito.when(campaignRepository.save(testCampaign)).thenReturn(testCampaign);
    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((Campaign) args[1]).setFundingGoals(CampaignUtils.TEST_FUNDING_GOALS);
              ((Campaign) args[1])
                  .setMinInvestment(PlatformSettingsUtils.TEST_PLATFORM_MINIMUMIM_INVESTMENT);
              return null;
            })
        .when(modelMapperBlankString)
        .map(campaignPatchDto, testCampaign);

    campaignPatchDto.setMinInvestment(new BigDecimal("1000"));
    when(platformSettingsService.getCurrentPlatformSettings())
        .thenReturn(PlatformSettingsUtils.TEST_PLATFORM_SETTINGS_DTO);

    CampaignResponseDto campaignResponseDto =
        campaignServiceImpl.patchCampaign(TEST_URL_FRIENDLY_NAME, campaignPatchDto);
    assertEquals(CampaignUtils.TEST_FUNDING_GOALS, campaignResponseDto.getFundingGoals());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void PatchCampaign_Should_Throw_ForbiddenOperationException() {
    Campaign testCampaign = TEST_ACTIVE_CAMPAIGN.toBuilder().build();
    CampaignPatchDto campaignPatchDto = CampaignUtils.TEST_CAMPAIGN_PATCH_DTO_FUNDING_GOALS();

    PowerMockito.when(
            campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_ACTIVE_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.patchCampaign(TEST_ACTIVE_URL_FRIENDLY_NAME, campaignPatchDto);
  }

  @Test
  public void CreateCampaign_Should_CreateCampaign() {
    when(companyService.findByIdOrThrowException(anyLong()))
        .thenReturn(CompanyUtils.getCompanyMocked());
    when(campaignRepository.findExistingByCompany(getCompanyMocked())).thenReturn(Optional.empty());
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.empty());
    when(campaignStateService.getCampaignState(CampaignStateName.INITIAL))
        .thenReturn(TEST_CAMPAIGN_INITIAL_STATE);

    TEST_CAMPAIGN_DTO.setMinInvestment(new BigDecimal("1000"));
    when(platformSettingsService.getCurrentPlatformSettings())
        .thenReturn(PlatformSettingsUtils.TEST_PLATFORM_SETTINGS_DTO);

    when(campaignRepository.save(any())).thenReturn(TEST_CAMPAIGN);
    CampaignResponseDto campaignResponseDto = campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);
    assertEquals(CampaignUtils.TEST_NAME, campaignResponseDto.getName());

    verify(companyService, Mockito.times(1)).findByIdOrThrowException(anyLong());
    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME);
    verify(campaignRepository, Mockito.times(1)).save(any(Campaign.class));
  }

  @Test(expected = BadRequestException.class)
  public void
      CreateCampaign_Should_ThrowException_When_Min_Investment_Smaller_Than_PlatformMinimum() {
    when(campaignRepository.findExistingByCompany(getCompanyMocked())).thenReturn(Optional.empty());
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.empty());
    when(campaignRepository.save(TEST_CAMPAIGN)).thenReturn(TEST_CAMPAIGN);
    when(companyService.findByIdOrThrowException(anyLong()))
        .thenReturn(CompanyUtils.getCompanyMocked());

    TEST_CAMPAIGN_DTO.setMinInvestment(new BigDecimal("400"));
    when(platformSettingsService.getCurrentPlatformSettings())
        .thenReturn(PlatformSettingsUtils.TEST_PLATFORM_SETTINGS_DTO);

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);

    verify(companyService, Mockito.times(1)).findByIdOrThrowException(anyLong());
    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME);
    verify(campaignRepository, Mockito.times(1)).save(any(Campaign.class));
  }

  @Test(expected = ActiveCampaignAlreadyExistsException.class)
  public void
      CreateCampaign_Should_Throw_ActiveCampaignAlreadyExistsException_WhenCampaignNameExists() {
    when(companyService.findByIdOrThrowException(anyLong()))
        .thenReturn(CompanyUtils.getCompanyMocked());
    when(campaignRepository.findExistingByCompany(getCompanyMocked()))
        .thenReturn(Optional.of(TEST_CAMPAIGN));

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);
  }

  @Test(expected = CampaignNameAlreadyExistsException.class)
  public void
      CreateCampaign_Should_Throw_CampaignNameAlreadyExistsException_WhenCampaignNameExists() {
    when(companyService.findByIdOrThrowException(anyLong()))
        .thenReturn(CompanyUtils.getCompanyMocked());
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_CAMPAIGN.toBuilder().build()));

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);
  }

  @Test
  public void UploadMarketImage_Should_DeleteOldMarketImage_And_SaveToRepository() {
    AuthUtils.mockRequestAndContextEntrepreneur();
    Campaign campaign = getCampaignMocked();
    campaign.setMarketImageUrl(TEST_FEATURED_IMAGE_URL);
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(campaign));

    campaignServiceImpl.uploadMarketImage(TEST_URL_FRIENDLY_NAME, FileUtils.MOCK_FILE_VALID);

    verify(cloudObjectStorageService, times(1))
        .uploadAndReplace(
            TEST_FEATURED_IMAGE_URL, campaign.getMarketImageUrl(), FileUtils.MOCK_FILE_VALID);
    verify(campaignRepository, times(1)).save(campaign);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void UploadMarketImage_ShouldThrow_ForbiddenOperationException_CannotBeEdited() {
    Campaign campaign = getActiveCampaignMocked();
    campaign.setMarketImageUrl(TEST_FEATURED_IMAGE_URL);
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(campaign));
    campaignServiceImpl.uploadMarketImage(TEST_URL_FRIENDLY_NAME, FileUtils.MOCK_FILE_VALID);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void UploadMarketImage_ShouldThrow_ForbiddenOperationException_NotOwner() {
    Campaign campaign = getCampaignMocked();
    campaign.setMarketImageUrl(TEST_FEATURED_IMAGE_URL);
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(campaign));
    mockSecurityContext(TEST_USER_AUTH2);

    campaignServiceImpl.uploadMarketImage(TEST_URL_FRIENDLY_NAME, FileUtils.MOCK_FILE_VALID);
  }

  @Test
  public void GetMarketImage_Should_ReturnMarketImage() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    when(cloudObjectStorageService.downloadFileDto(TEST_MARKET_IMAGE_UTL))
        .thenReturn(FileUtils.TEST_FILE_DTO);

    FileDto returnFileDto = campaignServiceImpl.downloadMarketImage(TEST_URL_FRIENDLY_NAME);

    assertEquals(FileUtils.TEST_FILE_DTO, returnFileDto);
    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME);
    verify(cloudObjectStorageService, Mockito.times(1)).downloadFileDto(TEST_MARKET_IMAGE_UTL);
  }

  @Test(expected = EntityNotFoundException.class)
  public void GetMarketImage_Should_Throw_EntityNotFoundException() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    doThrow(EntityNotFoundException.class).when(cloudObjectStorageService).downloadFileDto(any());

    campaignServiceImpl.downloadMarketImage(TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void DeleteMarketImage_Should_DeleteMarketImage() {
    AuthUtils.mockRequestAndContextEntrepreneur();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    doNothing().when(cloudObjectStorageService).delete(TEST_MARKET_IMAGE_UTL);

    campaignServiceImpl.deleteMarketImage(TEST_URL_FRIENDLY_NAME);

    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME);
    verify(cloudObjectStorageService, Mockito.times(1)).delete(TEST_MARKET_IMAGE_UTL);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void DeleteMarketImage_ShouldThrow_ForbiddenOperationException_CannotBeEdited() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getActiveCampaignMocked()));
    campaignServiceImpl.deleteMarketImage(TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void DeleteMarketImage_ShouldThrow_ForbiddenOperationException_NotOwner() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    mockSecurityContext(TEST_USER_AUTH2);
    campaignServiceImpl.deleteMarketImage(TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void GetActiveCampaignDto_Should_Return_Campaign() {
    when(companyService.findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId()))
        .thenReturn(getCompanyMocked());
    Campaign campaign = getCampaignMocked();
    when(campaignRepository.findExistingByCompany(getCompanyMocked()))
        .thenReturn(Optional.of(campaign));

    campaignServiceImpl.getActiveCampaignDto();

    verify(companyService, Mockito.times(1))
        .findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId());
    verify(campaignTopicService, times(1)).getTopicStatus(campaign);
    verify(campaignRepository, Mockito.times(1)).findExistingByCompany(getCompanyMocked());
  }

  @Test(expected = EntityNotFoundException.class)
  public void
      GetActiveCampaignForCompany_Should_Throw_EntityNotFoundException_When_No_Active_Campaign() {
    when(companyService.findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId()))
        .thenReturn(getCompanyMocked());

    campaignServiceImpl.getActiveCampaignForCompany();
  }

  @Test(expected = EntityNotFoundException.class)
  public void
      GetActiveCampaignForCompany_Should_Throw_EntityNotFoundException_When_No_Company_Of_Auth_User() {
    when(companyService.findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId()))
        .thenThrow(new EntityNotFoundException());
    campaignServiceImpl.getActiveCampaignForCompany();
  }

  @Test
  public void GetCampaignDtoByUrlFriendlyName_Should_Return_Campaign() {
    AuthUtils.mockRequestAndContextEntrepreneur();

    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_CAMPAIGN));

    CampaignResponseDto campaignDto =
        campaignServiceImpl.getCampaignDtoByUrlFriendlyName(TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_URL_FRIENDLY_NAME, campaignDto.getUrlFriendlyName());
  }

  @Test
  public void GetCampaignDtoByUrlFriendlyName_Should_Return_AuditCampaign() {
    AuthUtils.mockRequestAndContextAdmin();

    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_AUDIT_CAMPAIGN));
    when(auditService.findPendingAuditByCampaignOrThrowException(TEST_AUDIT_CAMPAIGN))
        .thenReturn(AuditUtils.TEST_PENDING_REQUEST_AUDIT);

    CampaignResponseDto campaignDto =
        campaignServiceImpl.getCampaignDtoByUrlFriendlyName(TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_URL_FRIENDLY_NAME, campaignDto.getUrlFriendlyName());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void
      GetCampaignDtoByUrlFriendlyName_Should_Throw_ForbiddenOperationException_When_AuthenticationAuth_Not_Auditor_Of_Campaign() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_AUDIT_CAMPAIGN));
    when(auditService.findPendingAuditByCampaignOrThrowException(TEST_AUDIT_CAMPAIGN))
        .thenReturn(AuditUtils.TEST_PENDING_REQUEST_AUDIT);

    campaignServiceImpl.getCampaignDtoByUrlFriendlyName(TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void DeleteCampaign_Should_SetStateDeleted() {
    AuthUtils.mockRequestAndContextEntrepreneur();
    Campaign testCampaign = getCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));
    when(otpService.validate(
            AuthenticationUtil.getAuthentication().getAuth(),
            new TwoFADto(OTPUtils.TEST_TOTP_CODE_1, null)))
        .thenReturn(true);
    when(campaignStateService.getCampaignState(CampaignStateName.DELETED))
        .thenReturn(TEST_CAMPAIGN_DELETED_STATE);

    campaignServiceImpl.delete(
        testCampaign.getUrlFriendlyName(), new TwoFADto(OTPUtils.TEST_TOTP_CODE_1, null));

    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName());
    verify(campaignRepository, Mockito.times(1)).save(testCampaign);
    assertEquals(CampaignStateName.DELETED, testCampaign.getCampaignState().getName());
  }

  @Test(expected = EntityNotFoundException.class)
  public void DeleteCampaign_Should_Throw_NotFoundException() {
    Campaign testCampaign = getCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.empty());
    when(otpService.validate(
            AuthenticationUtil.getAuthentication().getAuth(),
            new TwoFADto(OTPUtils.TEST_TOTP_CODE_1, null)))
        .thenReturn(true);
    campaignServiceImpl.delete(
        testCampaign.getUrlFriendlyName(), new TwoFADto(OTPUtils.TEST_TOTP_CODE_1, null));
  }

  @Test(expected = ForbiddenOperationException.class)
  public void DeleteCampaign_Should_Throw_ForbiddenException() {
    Campaign testCampaign = getCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));
    mockSecurityContext(TEST_USER_AUTH2);
    when(otpService.validate(
            AuthenticationUtil.getAuthentication().getAuth(),
            new TwoFADto(OTPUtils.TEST_TOTP_CODE_1, null)))
        .thenReturn(true);

    campaignServiceImpl.delete(
        testCampaign.getUrlFriendlyName(), new TwoFADto(OTPUtils.TEST_TOTP_CODE_1, null));
  }

  @Test
  public void sendNewCampaignOpportunityEmail_Should_SendEmail() {
    Campaign testCampaign = getCampaignMocked();
    when(campaignTopicService.getCampaignTopic(testCampaign.getUrlFriendlyName(), "OVERVIEW"))
        .thenReturn(TEST_CAMPAIGN_TOPIC_DTO);
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));

    campaignServiceImpl.sendNewCampaignOpportunityEmail(testCampaign);
  }

  @Test
  public void ConvertMoney_Should_Return_Percentage() {
    Campaign testCampaign = getActiveCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.convertMoneyToPercentageOfEquity(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(1000));
  }

  @Test(expected = EntityNotFoundException.class)
  public void ConvertMoney_Should_Throw_EntityNotFoundException() {
    Campaign testCampaign = getActiveCampaignMocked();

    campaignServiceImpl.convertMoneyToPercentageOfEquity(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(1000));
  }

  @Test(expected = BadRequestException.class)
  public void ConvertMoney_Should_Throw_BadRequestException_When_Campaign_Is_Not_Active() {
    Campaign testCampaign = getCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.convertMoneyToPercentageOfEquity(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(1000));
  }

  @Test(expected = BadRequestException.class)
  public void ConvertMoney_Should_Throw_BadRequestException_When_Money_Is_Negative_Value() {
    Campaign testCampaign = getActiveCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.convertMoneyToPercentageOfEquity(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(-1000));
  }

  @Test(expected = BadRequestException.class)
  public void
      ConvertMoney_Should_Throw_BadRequestException_When_Money_Smaller_Than_Min_Investment() {
    Campaign testCampaign = getActiveCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.convertMoneyToPercentageOfEquity(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(550));
  }

  @Test(expected = BadRequestException.class)
  public void
      ConvertMoney_Should_Throw_BadRequestException_When_Money_Greater_Than_Max_Investment() {
    Campaign testCampaign = getActiveCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.convertMoneyToPercentageOfEquity(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(300000));
  }

  @Test
  public void ConvertPercentage_Should_Return_Money() {
    Campaign testCampaign = getActiveCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.convertPercentageOfEquityToMoney(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(1.5));
  }

  @Test(expected = EntityNotFoundException.class)
  public void ConvertPercentage_Should_Throw_EntityNotFoundException() {
    Campaign testCampaign = getActiveCampaignMocked();

    campaignServiceImpl.convertPercentageOfEquityToMoney(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(1.5));
  }

  @Test(expected = BadRequestException.class)
  public void ConvertPercentage_Should_Throw_BadRequestException_When_Campaign_Is_Not_Active() {
    Campaign testCampaign = getCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.convertPercentageOfEquityToMoney(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(1.5));
  }

  @Test(expected = BadRequestException.class)
  public void
      ConvertPercentage_Should_Throw_BadRequestException_When_Percentage_Is_Negative_Value() {
    Campaign testCampaign = getActiveCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.convertMoneyToPercentageOfEquity(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(-1.5));
  }

  @Test(expected = BadRequestException.class)
  public void
      ConvertPercentage_Should_Throw_BadRequestException_When_Percent_Greater_Than_Max_Equity_Offered() {
    Campaign testCampaign = getActiveCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.convertPercentageOfEquityToMoney(
        testCampaign.getUrlFriendlyName(), BigDecimal.valueOf(20.5));
  }

  @Test
  public void GetAvailableEquity_Should_Return_MaxEquity() {
    Campaign investableCampaign = getInvestableCampaignMocked();
    investableCampaign.setCollectedAmount(BigDecimal.ZERO);
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(
            investableCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(investableCampaign));
    assertEquals(
        BigDecimal.TEN,
        campaignServiceImpl.getAvailableEquity(investableCampaign.getUrlFriendlyName()));
  }

  @Test
  public void GetAvailableEquity_Should_Return_HalfOfMaxEquity() {
    Campaign investableCampaign = getInvestableCampaignMocked();
    investableCampaign.setCollectedAmount(BigDecimal.valueOf(500L));
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(
            investableCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(investableCampaign));
    assertEquals(
        BigDecimal.valueOf(5.),
        campaignServiceImpl.getAvailableEquity(investableCampaign.getUrlFriendlyName()));
  }

  @Test
  public void GetAvailableEquity_Should_Return_NoEquityLeft() {
    Campaign investableCampaign = getInvestableCampaignMocked();
    investableCampaign.setCollectedAmount(BigDecimal.valueOf(1000L));
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(
            investableCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(investableCampaign));
    assertEquals(
        BigDecimal.ZERO,
        campaignServiceImpl.getAvailableEquity(investableCampaign.getUrlFriendlyName()));
  }

  @Test
  public void GetAvailableInvestmentAmount_Should_Return_InvestableAmount() {
    Campaign investableCampaign = getInvestableCampaignMocked();
    investableCampaign.setCollectedAmount(BigDecimal.ZERO);
    assertEquals(
        BigDecimal.valueOf(1000),
        campaignServiceImpl.getMaximumInvestableAmount(investableCampaign));
  }

  @Test
  public void GetAvailableInvestmentAmount_Should_Return_HalfOfInvestableAmount() {
    Campaign investableCampaign = getInvestableCampaignMocked();
    investableCampaign.setCollectedAmount(BigDecimal.valueOf(500L));
    assertEquals(
        BigDecimal.valueOf(500L),
        campaignServiceImpl.getMaximumInvestableAmount(investableCampaign));
  }

  @Test
  public void GetAvailableEquity_Should_Return_NoInvestableAmountLeft() {
    Campaign investableCampaign = getInvestableCampaignMocked();
    investableCampaign.setCollectedAmount(BigDecimal.valueOf(1000L));
    assertEquals(
        BigDecimal.ZERO, campaignServiceImpl.getMaximumInvestableAmount(investableCampaign));
  }

  @Test
  public void changeCampaignStateOrThrow_ShouldChangeState() {
    Campaign campaign = TEST_CAMPAIGN;

    when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

    campaignServiceImpl.changeCampaignStateOrThrow(campaign, CampaignStateName.REVIEW_READY);
  }

  @Test
  public void launchCampaign_Should_Launch_Campaign() {
    mockRequestAndContextAdmin();
    Campaign campaign = TEST_READY_FOR_LAUNCH_CAMPAIGN.toBuilder().build();

    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(campaign));
    doNothing().when(campaignStateService).changeStateOrThrow(campaign, CampaignStateName.ACTIVE);
    when(campaignRepository.save(any(Campaign.class)))
        .thenReturn(TEST_ACTIVE_CAMPAIGN.toBuilder().build());
    when(campaignTopicService.getCampaignTopic(
            TEST_ACTIVE_URL_FRIENDLY_NAME, CampaignTopicUtil.TEST_CAMPAIGN_OVERVIEW_TOPIC_TYPE))
        .thenReturn(CampaignTopicUtil.TEST_CAMPAIGN_TOPIC_DTO);

    Campaign actualCampaign = campaignServiceImpl.launchCampaign(TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_CAMPAIGN_ACTIVE_STATE, actualCampaign.getCampaignState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void launchCampaign_Should_Throw_Forbidden_Operation() {
    Campaign campaign = TEST_READY_FOR_LAUNCH_CAMPAIGN.toBuilder().build();

    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(campaign));

    campaignServiceImpl.launchCampaign(TEST_URL_FRIENDLY_NAME);
  }
}
