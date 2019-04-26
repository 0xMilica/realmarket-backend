package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.PlatformSettingsDto;
import io.realmarket.propeler.model.PlatformSettings;
import io.realmarket.propeler.repository.PlatformSettingsRepository;
import io.realmarket.propeler.service.PlatformSettingsService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
public class PlatformSettingsServiceImpl implements PlatformSettingsService {

  private PlatformSettingsRepository platformSettingsRepository;

  @Autowired
  public PlatformSettingsServiceImpl(PlatformSettingsRepository platformSettingsRepository) {
    this.platformSettingsRepository = platformSettingsRepository;
  }

  @Override
  public PlatformSettingsDto getCurrentPlatformSettings() {
    PlatformSettings settings =
        platformSettingsRepository
            .findFirstByOrderById()
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.PLATFORM_SETTINGS_NOT_FOUND));
    return new PlatformSettingsDto(settings);
  }
}
