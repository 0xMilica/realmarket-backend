package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignInvestorDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignInvestor;
import io.realmarket.propeler.repository.CampaignInvestorRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CampaignInvestorTestUtils;
import io.realmarket.propeler.util.CampaignUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Optional;

import static io.realmarket.propeler.util.CampaignInvestorTestUtils.*;
import static io.realmarket.propeler.util.CampaignUtils.TEST_CAMPAIGN;
import static io.realmarket.propeler.util.CampaignUtils.TEST_URL_FRIENDLY_NAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignInvestorServiceImpl.class)
public class CampaignInvestorServiceImplTest {

  @Mock private CampaignInvestorRepository campaignInvestorRepository;

  @Mock private CampaignService campaignService;

  @Mock private ModelMapperBlankString modelMapperBlankString;

  @InjectMocks private CampaignInvestorServiceImpl campaignInvestorService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void CreateCampaignInvestor_Should_CreateInvestor() throws Exception {
    Campaign campaign = CampaignUtils.getCampaignMocked();
    when(campaignService.findByUrlFriendlyNameOrThrowException(campaign.getUrlFriendlyName()))
        .thenReturn(campaign);
    campaignInvestorService.createCampaignInvestor(
        campaign.getUrlFriendlyName(), createMockCampaignInvestorDto());

    verify(campaignInvestorRepository, times(1)).save(any());
  }

  @Test
  public void DeleteCampaignInvestor_Should_DeleteInvestor() throws Exception {
    CampaignInvestor campaignInvestor = CampaignInvestorTestUtils.createMockCampaignInvestor();
    when(campaignInvestorRepository.findById(campaignInvestor.getId()))
        .thenReturn(Optional.of(campaignInvestor));
    campaignInvestorService.deleteCampaignInvestor(
        campaignInvestor.getCampaign().getUrlFriendlyName(), campaignInvestor.getId());

    verify(campaignInvestorRepository, times(1)).delete(campaignInvestor);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void DeleteCampaignInvestor_Should_ThrowException() {
    CampaignInvestor campaignInvestor = CampaignInvestorTestUtils.createMockCampaignInvestor();
    doThrow(ForbiddenOperationException.class).when(campaignService).throwIfNoAccess(any());
    when(campaignInvestorRepository.findById(campaignInvestor.getId()))
        .thenReturn(Optional.of(campaignInvestor));
    campaignInvestorService.deleteCampaignInvestor("TEST", campaignInvestor.getId());
  }

  @Test
  public void PatchCampaignInvestor_Should_PatchChanges() {
    CampaignInvestor campaignInvestor = CampaignInvestorTestUtils.createMockCampaignInvestor();
    CampaignInvestorDto campaignInvestorDto = CampaignInvestorTestUtils.mockInvestorPatchLastName();

    when(campaignInvestorRepository.findById(campaignInvestor.getId()))
        .thenReturn(Optional.of(campaignInvestor));

    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((CampaignInvestor) args[1]).setName(campaignInvestorDto.getName());
              return null;
            })
        .when(modelMapperBlankString)
        .map(campaignInvestorDto, campaignInvestor);

    campaignInvestorService.patchCampaignInvestor(
        campaignInvestor.getCampaign().getUrlFriendlyName(),
        campaignInvestor.getId(),
        campaignInvestorDto);

    assertEquals(TEST_CAMPAIGN_INVESTOR_NAME_2, campaignInvestor.getName());
    verify(modelMapperBlankString, times(1)).map(campaignInvestorDto, campaignInvestor);
    verify(campaignInvestorRepository, times(1)).save(campaignInvestor);
  }

  @Test
  public void GetCampaignInvestors_Should_ReturnListOfInvestors() {
    List<CampaignInvestor> campaignInvestorsList = mockInvestorList();
    when(campaignService.findByUrlFriendlyNameOrThrowException(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_CAMPAIGN);
    when(campaignInvestorRepository.findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(
            TEST_URL_FRIENDLY_NAME))
        .thenReturn(campaignInvestorsList);

    List<CampaignInvestor> ret =
        campaignInvestorService.getCampaignInvestors(TEST_URL_FRIENDLY_NAME);
    assertEquals(campaignInvestorsList, ret);
  }
}
