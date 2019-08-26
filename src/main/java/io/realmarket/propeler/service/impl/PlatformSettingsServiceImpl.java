package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CurrencyResponseDto;
import io.realmarket.propeler.model.Country;
import io.realmarket.propeler.repository.CountryRepository;
import io.realmarket.propeler.service.PlatformSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PlatformSettingsServiceImpl implements PlatformSettingsService {

  @Value("${app.investment.minimum}")
  private BigDecimal minimumInvestment;

  @Value("${app.locale.currency.code}")
  private String currencyCode;

  @Value("${app.locale.language}")
  private String localeLanguage;

  @Value("${app.locale.country}")
  private String localeCountry;

  private CountryRepository countryRepository;

  @Autowired
  public PlatformSettingsServiceImpl(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  @Override
  public Map<String, Object> getPlatformSettings() {
    Map<String, Object> platformSettings = new HashMap<>();
    platformSettings.put("countries", getCountries());
    platformSettings.put("platformMinimumInvestment", getPlatformMinimumInvestment());
    platformSettings.put("platformCurrency", getPlatformCurrency());
    return platformSettings;
  }

  @Override
  public List<Country> getCountries() {
    return countryRepository.findAll();
  }

  @Override
  public BigDecimal getPlatformMinimumInvestment() {
    return minimumInvestment;
  }

  @Override
  public CurrencyResponseDto getPlatformCurrency() {
    return new CurrencyResponseDto(
        Currency.getInstance(currencyCode), localeLanguage, localeCountry);
  }
}
