package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.repository.CampaignTopicRepository;
import io.realmarket.propeler.repository.CampaignTopicTypeRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignTopicImageService;
import io.realmarket.propeler.service.exception.CampaignTopicTypeNotExistException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;

import static io.realmarket.propeler.util.CampaignTopicUtil.*;
import static io.realmarket.propeler.util.CampaignUtils.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignTopicServiceImpl.class)
public class CampaignTopicServiceImplTest {

  @InjectMocks CampaignTopicServiceImpl campaignTopicService;

  @Mock CampaignTopicTypeRepository campaignTopicTypeRepository;
  @Mock private CampaignTopicRepository campaignTopicRepository;

  @Mock private CampaignService campaignService;
  @Mock private CampaignTopicImageService campaignTopicImageService;

  @Test
  public void CreateCampaignTopic_Should_SaveCampaignTopic() {
    when(campaignTopicTypeRepository.findByNameIgnoreCase(any()))
        .thenReturn(Optional.of(TEST_CAMPAIGN_TOPIC_TYPE));
    when(campaignService.findByUrlFriendlyNameOrThrowException(any())).thenReturn(TEST_CAMPAIGN);
    doNothing().when(campaignService).throwIfNoAccess(TEST_CAMPAIGN);
    when(campaignTopicRepository.save(any())).thenReturn(TEST_CAMPAIGN_TOPIC);

    campaignTopicService.createCampaignTopic(
        TEST_URL_FRIENDLY_NAME, TEST_CAMPAIGN_TOPIC_TYPE_NAME, TEST_CAMPAIGN_TOPIC_DTO);

    verify(campaignTopicTypeRepository, times(1)).findByNameIgnoreCase(any());
    verify(campaignService, times(1)).findByUrlFriendlyNameOrThrowException(any());
    verify(campaignTopicRepository, times(1)).save(any());
  }

  @Test(expected = EntityNotFoundException.class)
  public void CreateCampaignTopic_Should_Throw_EntityNotFoundException() {
    when(campaignTopicTypeRepository.findByNameIgnoreCase(any())).thenReturn(Optional.empty());

    campaignTopicService.createCampaignTopic(
        TEST_URL_FRIENDLY_NAME, TEST_CAMPAIGN_TOPIC_TYPE_NAME, TEST_CAMPAIGN_TOPIC_DTO);
  }

  @Test
  public void GetCampaignTopic_Should_Return_CampaignTopicDto() {
    when(campaignTopicTypeRepository.findByNameIgnoreCase(any()))
        .thenReturn(Optional.of(TEST_CAMPAIGN_TOPIC_TYPE));
    when(campaignService.findByUrlFriendlyNameOrThrowException(any())).thenReturn(TEST_CAMPAIGN);
    doNothing().when(campaignService).throwIfNoAccess(TEST_CAMPAIGN);
    when(campaignTopicRepository.findByCampaignAndCampaignTopicType(any(), any()))
        .thenReturn(Optional.of(TEST_CAMPAIGN_TOPIC));

    campaignTopicService.getCampaignTopic(TEST_URL_FRIENDLY_NAME, TEST_CAMPAIGN_TOPIC_TYPE_NAME);

    verify(campaignTopicTypeRepository, times(1)).findByNameIgnoreCase(any());
    verify(campaignService, times(1)).findByUrlFriendlyNameOrThrowException(any());
    verify(campaignTopicRepository, times(1)).findByCampaignAndCampaignTopicType(any(), any());
  }

  @Test(expected = EntityNotFoundException.class)
  public void GetCampaignTopic_Throw_EntityNotFoundException() {
    when(campaignTopicTypeRepository.findByNameIgnoreCase(any()))
        .thenReturn(Optional.of(TEST_CAMPAIGN_TOPIC_TYPE));
    when(campaignService.findByUrlFriendlyNameOrThrowException(any())).thenReturn(TEST_CAMPAIGN);
    doNothing().when(campaignService).throwIfNoAccess(TEST_CAMPAIGN);
    when(campaignTopicRepository.findByCampaignAndCampaignTopicType(any(), any()))
        .thenReturn(Optional.empty());

    campaignTopicService.getCampaignTopic(TEST_URL_FRIENDLY_NAME, TEST_CAMPAIGN_TOPIC_TYPE_NAME);
  }

  @Test(expected = CampaignTopicTypeNotExistException.class)
  public void GetCampaignTopic_Throw_CampaignTopicTypeNotExistException() {
    when(campaignService.findByUrlFriendlyNameOrThrowException(any())).thenReturn(TEST_CAMPAIGN);
    doNothing().when(campaignService).throwIfNoAccess(TEST_CAMPAIGN);
    when(campaignTopicRepository.findByCampaignAndCampaignTopicType(any(), any()))
        .thenReturn(Optional.empty());

    campaignTopicService.getCampaignTopic(TEST_URL_FRIENDLY_NAME, TEST_CAMPAIGN_TOPIC_TYPE_NAME);
  }

  @Test
  public void UpdateCampaignTopic_Should_UpdateContent() {
    when(campaignTopicTypeRepository.findByNameIgnoreCase(any()))
        .thenReturn(Optional.of(TEST_CAMPAIGN_TOPIC_TYPE));
    when(campaignService.findByUrlFriendlyNameOrThrowException(any())).thenReturn(TEST_CAMPAIGN);
    doNothing().when(campaignService).throwIfNoAccess(TEST_CAMPAIGN);
    when(campaignTopicRepository.findByCampaignAndCampaignTopicType(any(), any()))
        .thenReturn(Optional.of(TEST_CAMPAIGN_TOPIC));
    when(campaignTopicRepository.save(any())).thenReturn(TEST_CAMPAIGN_TOPIC);
    doNothing().when(campaignTopicImageService).removeObsoleteImages(TEST_CAMPAIGN_TOPIC);

    campaignTopicService.updateCampaignTopic(
        TEST_URL_FRIENDLY_NAME, TEST_CAMPAIGN_TOPIC_TYPE_NAME, TEST_CAMPAIGN_TOPIC_DTO);

    verify(campaignTopicTypeRepository, times(1)).findByNameIgnoreCase(any());
    verify(campaignService, times(1)).findByUrlFriendlyNameOrThrowException(any());
    verify(campaignTopicRepository, times(1)).save(any());
    verify(campaignTopicRepository, times(1)).findByCampaignAndCampaignTopicType(any(), any());
  }

  @Test
  public void GetTopicStatus_Should_ReturnStatuses() {
    Campaign campaign = getCampaignMocked();
    when(campaignTopicTypeRepository.findAll()).thenReturn(TEST_CAMPAIGN_TOPIC_TYPE_LIST_1);
    when(campaignTopicRepository.selectAllTopicsByCampaign(campaign))
        .thenReturn(TEST_CAMPAIGN_TOPIC_TYPE_LIST_2);

    Map<String, Boolean> topicStatus = campaignTopicService.getTopicStatus(campaign);

    assertTrue(topicStatus.get(TEST_CAMPAIGN_TOPIC_TYPE_NAME.toLowerCase()));
    assertFalse(topicStatus.get(TEST_CAMPAIGN_TOPIC_TYPE_NAME_2.toLowerCase()));
  }
}
