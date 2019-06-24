package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.repository.CampaignUpdateRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignUpdateImageService;
import io.realmarket.propeler.util.CampaignUpdateUtils;
import io.realmarket.propeler.util.CampaignUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.util.CampaignUtils.TEST_CAMPAIGN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignUpdateServiceImpl.class)
public class CampaignUpdateServiceImplTest {

  @Mock private CampaignUpdateRepository campaignUpdateRepository;
  @Mock private CampaignService campaignService;
  @Mock private CampaignUpdateImageService campaignUpdateImageService;

  @InjectMocks private CampaignUpdateServiceImpl campaignUpdateServiceImpl;

  @Test
  public void CreateCampaignUpdate_Should_CreateCampaignUpdate() {

    when(campaignService.findByUrlFriendlyNameOrThrowException(any()))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);

    doNothing().when(campaignService).throwIfNoAccess(TEST_CAMPAIGN);
    doNothing().when(campaignService).throwIfNotActive(TEST_CAMPAIGN);

    when(campaignUpdateRepository.save(any())).thenReturn(CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE);

    campaignUpdateServiceImpl.createCampaignUpdate(
        CampaignUtils.TEST_URL_FRIENDLY_NAME, CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE_DTO);

    verify(campaignService, times(1)).findByUrlFriendlyNameOrThrowException(any());
    verify(campaignUpdateRepository, times(1)).save(any());
  }

  @Test(expected = EntityNotFoundException.class)
  public void CreateCampaignUpdate_Should_Throw_EntityNotFoundException() {
    Mockito.doThrow(EntityNotFoundException.class)
        .when(campaignService)
        .findByUrlFriendlyNameOrThrowException(any());

    campaignUpdateServiceImpl.createCampaignUpdate(
        CampaignUtils.TEST_URL_FRIENDLY_NAME, CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE_DTO);
  }

  @Test
  public void UpdateCampaignUpdate_Should_UpdateCampaignUpdateContent() {

    when(campaignUpdateRepository.findById(any()))
        .thenReturn(Optional.of(CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE));

    doNothing().when(campaignService).throwIfNoAccess(TEST_CAMPAIGN);
    doNothing().when(campaignService).throwIfNotActive(TEST_CAMPAIGN);

    when(campaignUpdateRepository.save(any())).thenReturn(CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE);
    doNothing()
        .when(campaignUpdateImageService)
        .removeObsoleteImages(CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE);

    campaignUpdateServiceImpl.updateCampaignUpdate(
        CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE_ID,
        CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE_DTO_2);

    verify(campaignUpdateRepository, times(1)).findById(any());
    verify(campaignUpdateRepository, times(1)).save(any());
  }

  @Test(expected = EntityNotFoundException.class)
  public void UpdateCampaignUpdate_Should_Throw_EntityNotFoundException() {
    campaignUpdateServiceImpl.updateCampaignUpdate(
        CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE_ID,
        CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE_DTO_2);
  }

  @Test
  public void GetCampaignUpdate_Should_Return_CampaignUpdate() {
    when(campaignUpdateRepository.findById(any()))
        .thenReturn(Optional.of(CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE));

    campaignUpdateServiceImpl.getCampaignUpdate(CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE_ID);

    verify(campaignUpdateRepository, times(1)).findById(any());
  }

  @Test(expected = EntityNotFoundException.class)
  public void GetCampaignUpdate_Throw_EntityNotFoundException() {
    campaignUpdateServiceImpl.getCampaignUpdate(CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE_ID);
  }
}
