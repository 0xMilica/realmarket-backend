package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.PlatformSettingsController;
import io.realmarket.propeler.api.dto.CurrencyResponseDto;
import io.realmarket.propeler.model.Country;
import io.realmarket.propeler.service.PlatformSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/settings")
public class PlatformSettingsControllerImpl implements PlatformSettingsController {

  private final PlatformSettingsService platformSettingsService;

  public PlatformSettingsControllerImpl(PlatformSettingsService platformSettingsService) {
    this.platformSettingsService = platformSettingsService;
  }

  @GetMapping(value = "/countries")
  public ResponseEntity<List<Country>> getCountries() {
    return ResponseEntity.ok(platformSettingsService.getCountries());
  }

  @GetMapping(value = "/minimumInvestment")
  public ResponseEntity<BigDecimal> getMinimumInvestment() {
    return ResponseEntity.ok(platformSettingsService.getPlatformMinimumInvestment());
  }

  @GetMapping(value = "/currency")
  public ResponseEntity<CurrencyResponseDto> getCurrency() {
    return ResponseEntity.ok(
        new CurrencyResponseDto(platformSettingsService.getPlatformCurrency()));
  }
}
