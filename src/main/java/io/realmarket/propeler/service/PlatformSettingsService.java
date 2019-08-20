package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Country;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public interface PlatformSettingsService {

  List<Country> getCountries();

  BigDecimal getPlatformMinimumInvestment();

  Currency getPlatformCurrency();
}
