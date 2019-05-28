package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.PlatformSettingsDto;
import io.realmarket.propeler.model.Country;

import java.util.List;

public interface PlatformSettingsService {
  PlatformSettingsDto getCurrentPlatformSettings();

  List<Country> getCountries();
}
