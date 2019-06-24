package io.realmarket.propeler.util;

import io.realmarket.propeler.model.CampaignUpdateImage;

import java.util.Arrays;
import java.util.List;

public class CampaignUpdateImageUtils {

  public static final String TEST_EXISTING_URL = "FILE/TEST_EXISTING_URL.jpg";
  public static final String TEST_NOT_EXISTING_URL = "TEST_NOT_EXISTING_URL.JPG";

  public static final CampaignUpdateImage TEST_CAMPAIGN_UPDATE_IMAGE_EXISTING =
      CampaignUpdateImage.builder().url(TEST_EXISTING_URL).build();

  public static final CampaignUpdateImage TEST_CAMPAIGN_UPDATE_IMAGE_NOT_EXISTING =
      CampaignUpdateImage.builder().url(TEST_NOT_EXISTING_URL).build();

  public static final List<CampaignUpdateImage> TEST_CAMPAIGN_UPDATE_IMAGE_LIST =
      Arrays.asList(
          TEST_CAMPAIGN_UPDATE_IMAGE_EXISTING,
          TEST_CAMPAIGN_UPDATE_IMAGE_EXISTING,
          TEST_CAMPAIGN_UPDATE_IMAGE_NOT_EXISTING);
}
