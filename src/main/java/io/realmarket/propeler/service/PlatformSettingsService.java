package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CurrencyResponseDto;
import io.realmarket.propeler.model.Country;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PlatformSettingsService {

  Map<String, Object> getPlatformSettings();

  List<Country> getCountries();

  BigDecimal getPlatformMinimumInvestment();

  CurrencyResponseDto getPlatformCurrency();
}
