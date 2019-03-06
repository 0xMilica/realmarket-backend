package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.exception.CampaignNameAlreadyExistsException;
import io.realmarket.propeler.service.impl.CampaignServiceImpl;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.unit.util.CampaignUtils;
import io.realmarket.propeler.unit.util.CompanyUtils;
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

import static io.realmarket.propeler.unit.util.CampaignUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignServiceImpl.class)
public class CampaignServiceImplTest {
  @Mock CampaignRepository campaignRepository;

  @Mock CompanyService companyService;

  @Mock private ModelMapperBlankString modelMapperBlankString;

  @InjectMocks private CampaignServiceImpl campaignServiceImpl;

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
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.empty());
    when(campaignRepository.save(TEST_CAMPAIGN)).thenReturn(TEST_CAMPAIGN);
    when(companyService.findByIdOrThrowException(anyLong()))
        .thenReturn(CompanyUtils.getCompanyMocked());

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);

    verify(companyService, Mockito.times(1)).findByIdOrThrowException(anyLong());
    verify(campaignRepository, Mockito.times(1)).findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME);
    verify(campaignRepository, Mockito.times(1)).save(TEST_CAMPAIGN);
  }

  @Test(expected = CampaignNameAlreadyExistsException.class)
  public void
      CreateCampaign_Should_Throw_CampaignNameAlreadyExistsException_WhenCampaignNameExists() {
    when(campaignRepository.findByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_CAMPAIGN.toBuilder().build()));

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);
  }
}
