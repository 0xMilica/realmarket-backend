package io.realmarket.propeler.util;

import io.realmarket.propeler.model.CampaignTopicImage;

import java.util.Arrays;
import java.util.List;

public class CampaignTopicImageUtil {
  public static final String TEST_EXISTING_URL = "FILE/TEST_EXISTING_URL.jpg";
  public static final String TEST_NOT_EXISTING_URL = "TEST_NOT_EXISTING_URL.JPG";

  public static final CampaignTopicImage TEST_CAMPAIGN_TOPIC_IMAGE_EXISTING =
      CampaignTopicImage.builder().url(TEST_EXISTING_URL).build();

  public static final CampaignTopicImage TEST_CAMPAIGN_TOPIC_IMAGE_NOT_EXISTING =
      CampaignTopicImage.builder().url(TEST_NOT_EXISTING_URL).build();

  public static final List<CampaignTopicImage> TEST_CAMPAIGN_TOPIC_IMAGE_LIST =
      Arrays.asList(
          TEST_CAMPAIGN_TOPIC_IMAGE_EXISTING,
          TEST_CAMPAIGN_TOPIC_IMAGE_EXISTING,
          TEST_CAMPAIGN_TOPIC_IMAGE_NOT_EXISTING);
}
