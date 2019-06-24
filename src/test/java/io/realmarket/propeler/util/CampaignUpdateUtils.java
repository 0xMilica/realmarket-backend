package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CampaignUpdateDto;
import io.realmarket.propeler.model.CampaignUpdate;

import java.time.Instant;

public class CampaignUpdateUtils {

  public static final Long TEST_CAMPAIGN_UPDATE_ID = 1L;
  public static final String TEST_CAMPAIGN_UPDATE_TITLE = "TEST_CAMPAIGN_UPDATE_TITLE";
  public static final String TEST_CAMPAIGN_UPDATE_CONTENT = "TEST_CAMPAIGN_UPDATE_CONTENT";

  public static final CampaignUpdate TEST_CAMPAIGN_UPDATE =
      CampaignUpdate.builder()
          .title(TEST_CAMPAIGN_UPDATE_TITLE)
          .content(TEST_CAMPAIGN_UPDATE_CONTENT)
          .campaign(CampaignUtils.TEST_CAMPAIGN)
          .postDate(Instant.now())
          .build();

  public static final CampaignUpdateDto TEST_CAMPAIGN_UPDATE_DTO =
      new CampaignUpdateDto(TEST_CAMPAIGN_UPDATE_TITLE, TEST_CAMPAIGN_UPDATE_CONTENT);

  public static final CampaignUpdateDto TEST_CAMPAIGN_UPDATE_DTO_2 =
      new CampaignUpdateDto(null, TEST_CAMPAIGN_UPDATE_CONTENT);
}
