package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.PlatformSettingsController;
import io.realmarket.propeler.api.dto.PlatformSettingsDto;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.PlatformSettingsService;
import io.realmarket.propeler.service.TwoFactorAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
public class PlatformSettingsControllerImpl implements PlatformSettingsController {

  private final AuthService authService;
  private final PersonService personService;
  private final TwoFactorAuthService twoFactorAuthService;
  private final PlatformSettingsService platformSettingsService;

  public PlatformSettingsControllerImpl(
      AuthService authService,
      PersonService personService,
      TwoFactorAuthService twoFactorAuthService,
      PlatformSettingsService platformSettingsService) {
    this.authService = authService;
    this.personService = personService;
    this.twoFactorAuthService = twoFactorAuthService;
    this.platformSettingsService = platformSettingsService;
  }

  @GetMapping()
  public ResponseEntity<PlatformSettingsDto> getCurrentPlatformSettings() {
    return ResponseEntity.ok(platformSettingsService.getCurrentPlatformSettings());
  }
}
