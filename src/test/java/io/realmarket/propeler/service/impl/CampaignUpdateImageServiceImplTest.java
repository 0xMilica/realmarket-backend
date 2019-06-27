package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.CampaignUpdate;
import io.realmarket.propeler.repository.CampaignUpdateImageRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignUpdateService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.util.CampaignUpdateImageUtils;
import io.realmarket.propeler.util.CampaignUpdateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;

import static io.realmarket.propeler.util.CampaignTopicImageUtil.TEST_EXISTING_URL;
import static io.realmarket.propeler.util.CampaignUtils.TEST_CAMPAIGN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignUpdateImageServiceImpl.class)
public class CampaignUpdateImageServiceImplTest {

  @Mock private CampaignUpdateImageRepository campaignUpdateImageRepository;
  @Mock private CampaignService campaignService;
  @Mock private CampaignUpdateService campaignUpdateService;
  @Mock private CloudObjectStorageService cloudObjectStorageService;

  @InjectMocks private CampaignUpdateImageServiceImpl campaignUpdateImageService;

  @Test
  public void RemoveObsoleteImages_Should_RemoveImages() {
    when(campaignUpdateImageRepository.findByCampaignUpdate(any()))
        .thenReturn(CampaignUpdateImageUtils.TEST_CAMPAIGN_UPDATE_IMAGE_LIST);
    doNothing().when(cloudObjectStorageService).delete(any());
    doNothing().when(campaignUpdateImageRepository).delete(any());

    campaignUpdateImageService.removeObsoleteImages(
        CampaignUpdate.builder().content(TEST_EXISTING_URL).build());

    verify(cloudObjectStorageService, times(1)).delete(any());
    verify(campaignUpdateImageRepository, times(1)).delete(any());
  }

  @Test
  public void uploadImage_Should_UploadImageToCloud() {
    when(campaignUpdateService.findByIdOrThrowException(any()))
        .thenReturn(CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE);
    doNothing().when(campaignService).throwIfNoAccess(TEST_CAMPAIGN);
    doNothing().when(campaignService).throwIfNotActive(TEST_CAMPAIGN);

    when(campaignUpdateImageRepository.save(any()))
        .thenReturn(CampaignUpdateImageUtils.TEST_CAMPAIGN_UPDATE_IMAGE_EXISTING);
    doNothing().when(cloudObjectStorageService).upload(any(), any());

    campaignUpdateImageService.uploadImage(
        CampaignUpdateUtils.TEST_CAMPAIGN_UPDATE_ID,
        new MockMultipartFile(
            CampaignUpdateImageUtils.TEST_EXISTING_URL,
            CampaignUpdateImageUtils.TEST_EXISTING_URL,
            "image/jpeg",
            CampaignUpdateImageUtils.TEST_EXISTING_URL.getBytes()));
  }

  @Test
  public void RemoveImages_Should_RemoveImages() {
    when(campaignUpdateImageRepository.findByCampaignUpdate(any()))
        .thenReturn(CampaignUpdateImageUtils.TEST_CAMPAIGN_UPDATE_IMAGE_LIST);
    doNothing().when(cloudObjectStorageService).delete(any());
    doNothing().when(campaignUpdateImageRepository).delete(any());

    campaignUpdateImageService.removeObsoleteImages(
        CampaignUpdate.builder().content(TEST_EXISTING_URL).build());

    verify(cloudObjectStorageService, times(1)).delete(any());
    verify(campaignUpdateImageRepository, times(1)).delete(any());
  }
}
