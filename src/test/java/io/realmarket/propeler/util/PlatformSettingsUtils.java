package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.PlatformSettingsDto;

import java.math.BigDecimal;

public class PlatformSettingsUtils {

  public static BigDecimal TEST_PLATFORM_MINIMUMIM_INVESTMENT = new BigDecimal("500");
  public static PlatformSettingsDto TEST_PLATFORM_SETTINGS_DTO =
      PlatformSettingsDto.builder()
          .platformMinInvestment(TEST_PLATFORM_MINIMUMIM_INVESTMENT)
          .build();
}
