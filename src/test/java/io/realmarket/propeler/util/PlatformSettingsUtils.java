package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CurrencyResponseDto;

import java.math.BigDecimal;
import java.util.Currency;

public class PlatformSettingsUtils {

  public static BigDecimal TEST_PLATFORM_MINIMUM_INVESTMENT = new BigDecimal("500");
  public static CurrencyResponseDto TEST_PLATFORM_CURRENCY =
      new CurrencyResponseDto(Currency.getInstance("EUR"), "de", "DE");
}
