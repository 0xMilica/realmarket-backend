package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;

public class CampaignUtils {
  public static final String TEST_URL_FRIENDLY_NAME = "TEST_URL_FRIENDLY_NAME";
  public static final String TEST_MARKET_IMAGE_UTL = "MARKET_IMAGE_URL";
  public static final Long TEST_FUNDING_GOALS = 58L;
  public static final CampaignState TEST_CAMPAIGN_STATE =
      new CampaignState(100L, CampaignStateName.INITIAL.toString());

  public static final Campaign TEST_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
          .fundingGoals(0L)
          .campaignState(TEST_CAMPAIGN_STATE)
          .build();
  public static final CampaignDto TEST_CAMPAIGN_DTO = new CampaignDto(TEST_CAMPAIGN);

  public static Campaign getCampaignMocked() {
    return Campaign.builder()
        .company(CompanyUtils.getCompanyMocked())
        .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
        .fundingGoals(0L)
        .marketImageUrl(TEST_MARKET_IMAGE_UTL)
        .build();
  }

  public static CampaignPatchDto TEST_CAMPAIGN_PATCH_DTO_FUNDING_GOALS() {
    return CampaignPatchDto.builder().fundingGoals(TEST_FUNDING_GOALS).build();
  }
}
