package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.ShareholderDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Shareholder;
import io.realmarket.propeler.repository.ShareholderRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CampaignUtils;
import io.realmarket.propeler.util.ShareholderTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Optional;

import static io.realmarket.propeler.util.CampaignUtils.TEST_CAMPAIGN;
import static io.realmarket.propeler.util.CampaignUtils.TEST_URL_FRIENDLY_NAME;
import static io.realmarket.propeler.util.ShareholderTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ShareholderServiceImpl.class)
public class ShareholderServiceImplTest {

  @Mock private ShareholderRepository shareholderRepository;

  @Mock private CampaignService campaignService;

  @Mock private ModelMapperBlankString modelMapperBlankString;

  @InjectMocks private ShareholderServiceImpl shareholderService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void CreateShareholder_Should_CreateShareholder() throws Exception {
    Campaign campaign = CampaignUtils.getCampaignMocked();
    when(campaignService.findByUrlFriendlyNameOrThrowException(campaign.getUrlFriendlyName()))
        .thenReturn(campaign);
    shareholderService.createShareholder(campaign.getUrlFriendlyName(), createMockShareholderDto());

    verify(shareholderRepository, times(1)).save(any());
  }

  @Test
  public void DeleteShareholder_Should_DeleteShareholder() throws Exception {
    Shareholder shareholder = ShareholderTestUtils.createMockShareholder();
    when(shareholderRepository.findById(shareholder.getId())).thenReturn(Optional.of(shareholder));
    shareholderService.deleteShareholder(
        shareholder.getCampaign().getUrlFriendlyName(), shareholder.getId());

    verify(shareholderRepository, times(1)).delete(shareholder);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void DeleteShareholder_Should_ThrowException() {
    Shareholder shareholder = ShareholderTestUtils.createMockShareholder();
    doThrow(ForbiddenOperationException.class).when(campaignService).throwIfNoAccess(any());
    when(shareholderRepository.findById(shareholder.getId())).thenReturn(Optional.of(shareholder));
    shareholderService.deleteShareholder("TEST", shareholder.getId());
  }

  @Test
  public void PatchShareholder_Should_PatchChanges() {
    Shareholder shareholder = ShareholderTestUtils.createMockShareholder();
    ShareholderDto shareholderDto = ShareholderTestUtils.mockShareholderPatchLastName();

    when(shareholderRepository.findById(shareholder.getId())).thenReturn(Optional.of(shareholder));

    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((Shareholder) args[1]).setName(shareholderDto.getName());
              return null;
            })
        .when(modelMapperBlankString)
        .map(shareholderDto, shareholder);

    shareholderService.patchShareholder(
        shareholder.getCampaign().getUrlFriendlyName(), shareholder.getId(), shareholderDto);

    assertEquals(TEST_CAMPAIGN_INVESTOR_NAME_2, shareholder.getName());
    verify(modelMapperBlankString, times(1)).map(shareholderDto, shareholder);
    verify(shareholderRepository, times(1)).save(shareholder);
  }

  @Test
  public void GetShareholder_Should_ReturnListOfShareholders() {
    List<Shareholder> shareholdersList = mockShareholderList();
    when(campaignService.findByUrlFriendlyNameOrThrowException(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_CAMPAIGN);
    when(shareholderRepository.findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(
            TEST_URL_FRIENDLY_NAME))
        .thenReturn(shareholdersList);

    List<Shareholder> ret = shareholderService.getShareholders(TEST_URL_FRIENDLY_NAME);
    assertEquals(shareholdersList, ret);
  }
}
