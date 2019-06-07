package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.ShareholderDto;
import io.realmarket.propeler.model.Shareholder;

import java.util.Arrays;
import java.util.List;

public class ShareholderTestUtils {

  public static final String TEST_SHAREHOLDER_NAME = "SHAREHOLDER_NAME";
  public static final String TEST_SHAREHOLDER_NAME_2 = "SHAREHOLDER_NAME_2";
  public static final String TEST_SHAREHOLDER_PICTURE_URL = "SHAREHOLDER_PIC_URL";

  public static Shareholder createMockShareholder() {
    return Shareholder.builder()
        .isAnonymous(false)
        .company(CompanyUtils.getCompanyMocked())
        .orderNumber(1)
        .name(TEST_SHAREHOLDER_NAME)
        .photoUrl(TEST_SHAREHOLDER_PICTURE_URL)
        .build();
  }

  public static ShareholderDto mockShareholderPatchLastName() {
    return ShareholderDto.builder().name(TEST_SHAREHOLDER_NAME_2).build();
  }

  public static List<Shareholder> mockShareholderList() {
    return Arrays.asList(createMockShareholder(), createMockShareholder());
  }

  public static ShareholderDto createMockShareholderDto() {
    return new ShareholderDto(createMockShareholder());
  }
}
