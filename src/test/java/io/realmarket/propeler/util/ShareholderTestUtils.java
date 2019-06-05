package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.ShareholderDto;
import io.realmarket.propeler.model.Shareholder;

import java.util.Arrays;
import java.util.List;

public class ShareholderTestUtils {

  public static final String TEST_CAMPAIGN_INVESTOR_NAME = "INVESTOR_NAME";
  public static final String TEST_CAMPAIGN_INVESTOR_NAME_2 = "INVESTOR_NAME_2";
  public static final String TEST_CAMPAIGN_INVESTOR_PICTURE_URL = "INVESTOR_PIC_URL";

  public static Shareholder createMockShareholder() {
    return Shareholder.builder()
        .isAnonymous(false)
        .campaign(CampaignUtils.TEST_CAMPAIGN)
        .orderNumber(1)
        .name(TEST_CAMPAIGN_INVESTOR_NAME)
        .photoUrl(TEST_CAMPAIGN_INVESTOR_PICTURE_URL)
        .build();
  }

  public static ShareholderDto mockShareholderPatchLastName() {
    return ShareholderDto.builder().name(TEST_CAMPAIGN_INVESTOR_NAME_2).build();
  }

  public static List<Shareholder> mockShareholderList() {
    return Arrays.asList(createMockShareholder(), createMockShareholder());
  }

  public static ShareholderDto createMockShareholderDto() {
    return new ShareholderDto(createMockShareholder());
  }
}
