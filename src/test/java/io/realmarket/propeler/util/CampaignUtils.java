package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;

public class CampaignUtils {

  public static final Long TEST_CAMPAIGN_ID = 1L;
  public static final String TEST_URL_FRIENDLY_NAME = "TEST_URL_FRIENDLY_NAME";
  public static final String TEST_ACTIVE_URL_FRIENDLY_NAME = "TEST_URL_FRIENDLY_NAME";
  public static final String TEST_MARKET_IMAGE_UTL = "MARKET_IMAGE_URL";
  public static final Long TEST_FUNDING_GOALS = 58L;
  public static final CampaignState TEST_CAMPAIGN_INITIAL_STATE =
      CampaignState.builder().name(CampaignStateName.INITIAL).build();
  public static final CampaignState TEST_CAMPAIGN_ACTIVE_STATE =
      CampaignState.builder().name(CampaignStateName.ACTIVE).build();
  public static final String TEST_NAME = "TEST_NAME";
  public static final CampaignState TEST_CAMPAIGN_DELETED_STATE =
      CampaignState.builder().name(CampaignStateName.DELETED).build();

  public static final Campaign TEST_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
          .name(TEST_NAME)
          .fundingGoals(0L)
          .campaignState(TEST_CAMPAIGN_INITIAL_STATE)
          .build();
  public static final CampaignDto TEST_CAMPAIGN_DTO = new CampaignDto(TEST_CAMPAIGN);

  public static Campaign getCampaignMocked() {
    return Campaign.builder()
        .company(CompanyUtils.getCompanyMocked())
        .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
        .fundingGoals(0L)
        .campaignState(TEST_CAMPAIGN_INITIAL_STATE)
        .marketImageUrl(TEST_MARKET_IMAGE_UTL)
        .build();
  }

  public static final Campaign TEST_ACTIVE_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_ACTIVE_URL_FRIENDLY_NAME)
          .fundingGoals(0L)
          .campaignState(TEST_CAMPAIGN_ACTIVE_STATE)
          .build();
  public static final CampaignDto TEST_ACTIVE_CAMPAIGN_DTO = new CampaignDto(TEST_ACTIVE_CAMPAIGN);

  public static Campaign getActiveCampaignMocked() {
    return Campaign.builder()
        .company(CompanyUtils.getCompanyMocked())
        .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
        .fundingGoals(0L)
        .campaignState(TEST_CAMPAIGN_ACTIVE_STATE)
        .marketImageUrl(TEST_MARKET_IMAGE_UTL)
        .build();
  }

  public static CampaignPatchDto TEST_CAMPAIGN_PATCH_DTO_FUNDING_GOALS() {
    return CampaignPatchDto.builder().fundingGoals(TEST_FUNDING_GOALS).build();
  }
}
