package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Country;
import io.realmarket.propeler.repository.CountryRepository;
import io.realmarket.propeler.service.PlatformSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Service
@Slf4j
public class PlatformSettingsServiceImpl implements PlatformSettingsService {

  @Value("${app.investment.minimum}")
  private BigDecimal minimumInvestment;

  @Value("${app.currency.code}")
  private String currencyCode;

  private CountryRepository countryRepository;

  @Autowired
  public PlatformSettingsServiceImpl(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
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
  public Currency getPlatformCurrency() {
    return Currency.getInstance(currencyCode);
  }
}
