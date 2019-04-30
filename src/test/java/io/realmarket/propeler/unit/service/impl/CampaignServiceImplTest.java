package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.service.CampaignTopicService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.exception.ActiveCampaignAlreadyExistsException;
import io.realmarket.propeler.service.exception.CampaignNameAlreadyExistsException;
import io.realmarket.propeler.service.impl.CampaignServiceImpl;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.unit.util.AuthUtils;
import io.realmarket.propeler.unit.util.CampaignUtils;
import io.realmarket.propeler.unit.util.CompanyUtils;
import io.realmarket.propeler.unit.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_USER_AUTH;
import static io.realmarket.propeler.unit.util.CampaignUtils.*;
import static io.realmarket.propeler.unit.util.CompanyUtils.TEST_FEATURED_IMAGE_URL;
import static io.realmarket.propeler.unit.util.CompanyUtils.getCompanyMocked;
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

  @InjectMocks private CampaignServiceImpl campaignServiceImpl;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void findByUrlFriendlyNameOrThrowException_Should_ReturnCampaign_IfUserExists() {
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_CAMPAIGN));

    Campaign retVal =
        campaignServiceImpl.findByUrlFriendlyNameOrThrowException(TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_CAMPAIGN, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FindByUrlFriendlyNameOrThrowException_Should_ThrowException_IfUserNotExists() {
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.empty());

    campaignServiceImpl.findByUrlFriendlyNameOrThrowException(TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void PatchCampaign_Should_CallModelMapper() {
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();
    CampaignPatchDto campaignPatchDto = CampaignUtils.TEST_CAMPAIGN_PATCH_DTO_FUNDING_GOALS();
    PowerMockito.when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(testCampaign));
    PowerMockito.when(campaignRepository.save(testCampaign)).thenReturn(testCampaign);
    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((Campaign) args[1]).setFundingGoals(CampaignUtils.TEST_FUNDING_GOALS);
              return null;
            })
        .when(modelMapperBlankString)
        .map(campaignPatchDto, testCampaign);

    CampaignDto campaignDto =
        campaignServiceImpl.patchCampaign(TEST_URL_FRIENDLY_NAME, campaignPatchDto);
    assertEquals(CampaignUtils.TEST_FUNDING_GOALS, campaignDto.getFundingGoals());
  }

  @Test
  public void CreateCampaign_Should_CreateCampaign() {
    when(campaignRepository.findByCompanyIdAndActiveTrue(TEST_CAMPAIGN_DTO.getCompanyId()))
        .thenReturn(Optional.empty());
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.empty());
    when(campaignRepository.save(TEST_CAMPAIGN)).thenReturn(TEST_CAMPAIGN);
    when(companyService.findByIdOrThrowException(anyLong()))
        .thenReturn(CompanyUtils.getCompanyMocked());

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);

    verify(companyService, Mockito.times(1)).findByIdOrThrowException(anyLong());
    verify(campaignRepository, Mockito.times(1)).findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME);
    verify(campaignRepository, Mockito.times(1)).save(any(Campaign.class));
  }

  @Test(expected = ActiveCampaignAlreadyExistsException.class)
  public void
      CreateCampaign_Should_Throw_ActiveCampaignAlreadyExistsException_WhenCampaignNameExists() {
    when(campaignRepository.findByCompanyIdAndActiveTrue(TEST_CAMPAIGN_DTO.getCompanyId()))
        .thenReturn(Optional.of(TEST_CAMPAIGN));

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);
  }

  @Test(expected = CampaignNameAlreadyExistsException.class)
  public void
      CreateCampaign_Should_Throw_CampaignNameAlreadyExistsException_WhenCampaignNameExists() {
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_CAMPAIGN.toBuilder().build()));

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);
  }

  @Test
  public void UploadMarketImage_Should_DeleteOldMarketImage_And_SaveToRepository() {
    Campaign campaign = getCampaignMocked();
    campaign.setMarketImageUrl(TEST_FEATURED_IMAGE_URL);
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(campaign));

    campaignServiceImpl.uploadMarketImage(TEST_URL_FRIENDLY_NAME, FileUtils.MOCK_FILE_VALID);

    verify(cloudObjectStorageService, times(1))
        .uploadAndReplace(
            TEST_FEATURED_IMAGE_URL, campaign.getMarketImageUrl(), FileUtils.MOCK_FILE_VALID);
    verify(campaignRepository, times(1)).save(campaign);
  }

  @Test
  public void GetMarketImage_Should_ReturnMarketImage() {
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    when(cloudObjectStorageService.downloadFileDto(TEST_MARKET_IMAGE_UTL))
        .thenReturn(FileUtils.TEST_FILE_DTO);

    FileDto returnFileDto = campaignServiceImpl.downloadMarketImage(TEST_URL_FRIENDLY_NAME);

    assertEquals(FileUtils.TEST_FILE_DTO, returnFileDto);
    verify(campaignRepository, Mockito.times(1)).findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME);
    verify(cloudObjectStorageService, Mockito.times(1)).downloadFileDto(TEST_MARKET_IMAGE_UTL);
  }

  @Test(expected = EntityNotFoundException.class)
  public void GetMarketImage_Should_Throw_EntityNotFoundException() {
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    doThrow(EntityNotFoundException.class).when(cloudObjectStorageService).downloadFileDto(any());

    campaignServiceImpl.downloadMarketImage(TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void DeleteMarketImage_Should_DeleteMarketImage() {
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    doNothing().when(cloudObjectStorageService).delete(TEST_MARKET_IMAGE_UTL);

    campaignServiceImpl.deleteMarketImage(TEST_URL_FRIENDLY_NAME);

    verify(campaignRepository, Mockito.times(1)).findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME);
    verify(cloudObjectStorageService, Mockito.times(1)).delete(TEST_MARKET_IMAGE_UTL);
  }

  @Test
  public void GetActiveCampaignDto_Should_Return_Campaign() {
    when(companyService.findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId()))
        .thenReturn(getCompanyMocked());
    Campaign campaign = getCampaignMocked();
    when(campaignRepository.findByCompanyIdAndActiveTrue(getCompanyMocked().getId()))
        .thenReturn(Optional.of(campaign));

    campaignServiceImpl.getActiveCampaignDto();

    verify(companyService, Mockito.times(1))
        .findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId());
    verify(campaignTopicService, times(1)).getTopicStatus(campaign);
    verify(campaignRepository, Mockito.times(1))
        .findByCompanyIdAndActiveTrue(getCompanyMocked().getId());
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
}
