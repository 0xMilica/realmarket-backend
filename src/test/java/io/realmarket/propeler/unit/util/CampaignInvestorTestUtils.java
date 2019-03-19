package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.CampaignInvestorDto;
import io.realmarket.propeler.model.CampaignInvestor;

import java.util.Arrays;
import java.util.List;

public class CampaignInvestorTestUtils {

  public static final String TEST_CAMPAIGN_INVESTOR_NAME = "INVESTOR_NAME";
  public static final String TEST_CAMPAIGN_INVESTOR_NAME_2 = "INVESTOR_NAME_2";
  public static final String TEST_CAMPAIGN_INVESTOR_PICTURE_URL = "INVESTOR_PIC_URL";

  public static CampaignInvestor createMockCampaignInvestor() {
    return CampaignInvestor.builder()
        .isAnonymous(false)
        .campaign(CampaignUtils.TEST_CAMPAIGN)
        .orderNumber(1)
        .name(TEST_CAMPAIGN_INVESTOR_NAME)
        .photoUrl(TEST_CAMPAIGN_INVESTOR_PICTURE_URL)
        .build();
  }

  public static CampaignInvestorDto mockInvestorPatchLastName() {
    return CampaignInvestorDto.builder().name(TEST_CAMPAIGN_INVESTOR_NAME_2).build();
  }

  public static List<CampaignInvestor> mockInvestorList() {
    return Arrays.asList(createMockCampaignInvestor(), createMockCampaignInvestor());
  }

  public static CampaignInvestorDto createMockCampaignInvestorDto() {
    return new CampaignInvestorDto(createMockCampaignInvestor());
  }
}
