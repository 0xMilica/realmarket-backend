package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.PlatformSettingsDto;
import io.realmarket.propeler.model.Country;
import io.realmarket.propeler.model.PlatformSettings;
import io.realmarket.propeler.repository.CountryRepository;
import io.realmarket.propeler.repository.PlatformSettingsRepository;
import io.realmarket.propeler.service.PlatformSettingsService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
public class PlatformSettingsServiceImpl implements PlatformSettingsService {

  private PlatformSettingsRepository platformSettingsRepository;
  private CountryRepository countryRepository;

  @Autowired
  public PlatformSettingsServiceImpl(
      PlatformSettingsRepository platformSettingsRepository, CountryRepository countryRepository) {
    this.platformSettingsRepository = platformSettingsRepository;
    this.countryRepository = countryRepository;
  }

  @Override
  public PlatformSettingsDto getCurrentPlatformSettings() {
    final PlatformSettings settings =
        platformSettingsRepository
            .findTopBy()
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.PLATFORM_SETTINGS_NOT_FOUND));
    return new PlatformSettingsDto(settings);
  }

  @Override
  public List<Country> getCountries() {
    return countryRepository.findAll();
  }
}
