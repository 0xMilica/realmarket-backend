package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CampaignTopicDto;
import io.realmarket.propeler.model.CampaignTopic;
import io.realmarket.propeler.model.CampaignTopicType;

import java.util.Arrays;
import java.util.List;

public class CampaignTopicUtil {

  public static final String TEST_CAMPAIGN_TOPIC_CONTENT = "TEST_CAMPAIGN_TOPIC_CONTENT";
  public static final Long TEST_CAMPAIGN_TOPIC_TYPE_ID = 10L;
  public static final String TEST_CAMPAIGN_TOPIC_TYPE_NAME = "TEST_CAMPAIGN_TOPIC_TYPE_NAME";
  public static final Long TEST_CAMPAIGN_TOPIC_TYPE_ID_2 = 12L;
  public static final String TEST_CAMPAIGN_TOPIC_TYPE_NAME_2 = "TEST_CAMPAIGN_TOPIC_TYPE_NAME_2";
  public static final CampaignTopicType TEST_CAMPAIGN_TOPIC_TYPE =
      CampaignTopicType.builder()
          .id(TEST_CAMPAIGN_TOPIC_TYPE_ID)
          .name(TEST_CAMPAIGN_TOPIC_TYPE_NAME)
          .build();
  public static final CampaignTopicType TEST_CAMPAIGN_TOPIC_TYPE_2 =
      CampaignTopicType.builder()
          .id(TEST_CAMPAIGN_TOPIC_TYPE_ID_2)
          .name(TEST_CAMPAIGN_TOPIC_TYPE_NAME_2)
          .build();
  public static final CampaignTopic TEST_CAMPAIGN_TOPIC =
      CampaignTopic.builder()
          .content(TEST_CAMPAIGN_TOPIC_CONTENT)
          .campaignTopicType(TEST_CAMPAIGN_TOPIC_TYPE)
          .campaign(CampaignUtils.TEST_CAMPAIGN)
          .build();

  public static final List<CampaignTopicType> TEST_CAMPAIGN_TOPIC_TYPE_LIST_1 =
      Arrays.asList(TEST_CAMPAIGN_TOPIC_TYPE, TEST_CAMPAIGN_TOPIC_TYPE_2);

  public static final List<CampaignTopicType> TEST_CAMPAIGN_TOPIC_TYPE_LIST_2 =
      Arrays.asList(TEST_CAMPAIGN_TOPIC_TYPE);

  public static final CampaignTopicDto TEST_CAMPAIGN_TOPIC_DTO =
      new CampaignTopicDto(TEST_CAMPAIGN_TOPIC_CONTENT);
}
