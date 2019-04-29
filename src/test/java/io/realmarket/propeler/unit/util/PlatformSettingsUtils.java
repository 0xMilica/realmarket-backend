package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.PlatformSettingsDto;
import io.realmarket.propeler.model.PlatformSettings;

import java.math.BigDecimal;

public class PlatformSettingsUtils {

  private static BigDecimal TEST_PLATFORM_MINIMUMIM_INVESTMENT = new BigDecimal("500");
  public static PlatformSettingsDto TEST_PLATFORM_SETTINGS_DTO =
      PlatformSettingsDto.builder()
          .platformMinInvestment(TEST_PLATFORM_MINIMUMIM_INVESTMENT)
          .build();
}
