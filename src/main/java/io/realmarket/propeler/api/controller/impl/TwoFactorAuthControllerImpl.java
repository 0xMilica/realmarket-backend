package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.TwoFactorAuthController;
import io.realmarket.propeler.service.TwoFactorAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth/2fa")
@Slf4j
public class TwoFactorAuthControllerImpl implements TwoFactorAuthController {

  private final TwoFactorAuthService twoFactorAuthService;

  @Autowired
  public TwoFactorAuthControllerImpl(TwoFactorAuthService twoFactorAuthService) {
    this.twoFactorAuthService = twoFactorAuthService;
  }

  @PostMapping(value = "")
  public ResponseEntity login() {
    twoFactorAuthService.login();
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/secret")
  public ResponseEntity createSecret() {
    twoFactorAuthService.createSecret();
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/recovery_codes")
  public ResponseEntity createRecoveryCodes() {
    twoFactorAuthService.createRecoveryCodes();
    return ResponseEntity.ok().build();
  }
}
