package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.model.Campaign;

public class CampaignUtils {
  public static final String TEST_URL_FRIENDLY_NAME = "TEST_URL_FRIENDLY_NAME";
  public static final String TEST_MARKET_IMAGE_UTL = "MARKET_IMAGE_URL";
  public static final Long TEST_FUNDING_GOALS = 58L;

  public static final Campaign TEST_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
          .fundingGoals(0L)
          .build();

  public static Campaign getCampaignMocked() {
    return Campaign.builder()
        .company(CompanyUtils.getCompanyMocked())
        .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
        .fundingGoals(0L)
        .marketImageUrl(TEST_MARKET_IMAGE_UTL)
        .build();
  }

  public static final CampaignDto TEST_CAMPAIGN_DTO = new CampaignDto(TEST_CAMPAIGN);

  public static CampaignPatchDto TEST_CAMPAIGN_PATCH_DTO_FUNDING_GOALS() {
    return CampaignPatchDto.builder().fundingGoals(TEST_FUNDING_GOALS).build();
  }
}
