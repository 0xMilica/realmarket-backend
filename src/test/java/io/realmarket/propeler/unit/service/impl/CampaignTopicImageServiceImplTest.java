package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.model.CampaignTopic;
import io.realmarket.propeler.repository.CampaignTopicImageRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignTopicService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.impl.CampaignTopicImageServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_REQUEST;
import static io.realmarket.propeler.unit.util.CampaignTopicImageUtil.*;
import static io.realmarket.propeler.unit.util.CampaignTopicUtil.*;
import static io.realmarket.propeler.unit.util.CampaignUtils.TEST_CAMPAIGN;
import static io.realmarket.propeler.unit.util.CampaignUtils.TEST_URL_FRIENDLY_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignTopicImageServiceImpl.class)
public class CampaignTopicImageServiceImplTest {

  @InjectMocks CampaignTopicImageServiceImpl campaignTopicImageService;

  @Mock private CampaignTopicImageRepository campaignTopicImageRepository;

  @Mock private CampaignService campaignService;
  @Mock private CampaignTopicService campaignTopicService;
  @Mock private CloudObjectStorageService cloudObjectStorageService;

  @Test
  public void RemoveObsoleteImages_Should_RemoveImages() {
    when(campaignTopicImageRepository.findByCampaignTopic(any()))
        .thenReturn(TEST_CAMPAIGN_TOPIC_IMAGE_LIST);
    doNothing().when(cloudObjectStorageService).delete(any());
    doNothing().when(campaignTopicImageRepository).delete(any());

    campaignTopicImageService.removeObsoleteImages(
        CampaignTopic.builder().content(TEST_EXISTING_URL).build());

    verify(cloudObjectStorageService, times(1)).delete(any());
    verify(campaignTopicImageRepository, times(1)).delete(any());
  }

  @Test
  public void uploadImage_Should_UploadImageToCloud() {
    when(campaignTopicService.findByTopicTypeOrThrowException(any()))
        .thenReturn(TEST_CAMPAIGN_TOPIC_TYPE);
    when(campaignService.findByUrlFriendlyNameOrThrowException(any())).thenReturn(TEST_CAMPAIGN);
    doNothing().when(campaignService).throwIfNoAccess(TEST_CAMPAIGN);

    when(campaignTopicService.findByCampaignAndCampaignTopicTypeOrThrowException(any(), any()))
        .thenReturn(TEST_CAMPAIGN_TOPIC);

    when(campaignTopicImageRepository.save(any())).thenReturn(TEST_CAMPAIGN_TOPIC_IMAGE_EXISTING);
    doNothing().when(cloudObjectStorageService).upload(any(), any());

    campaignTopicImageService.uploadImage(
        TEST_REQUEST,
        TEST_URL_FRIENDLY_NAME,
        TEST_CAMPAIGN_TOPIC_TYPE_NAME,
        new MockMultipartFile(
            TEST_EXISTING_URL, TEST_EXISTING_URL, "image/jpeg", TEST_EXISTING_URL.getBytes()));
  }
}
