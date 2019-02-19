package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.TwoFactorAuthController;
import io.realmarket.propeler.api.dto.OTPWildcardResponseDto;
import io.realmarket.propeler.api.dto.TwoFASecretRequestDto;
import io.realmarket.propeler.api.dto.TwoFASecretResponseDto;
import io.realmarket.propeler.api.dto.TwoFATokenDto;
import io.realmarket.propeler.service.TwoFactorAuthService;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth/2fa")
@Slf4j
public class TwoFactorAuthControllerImpl implements TwoFactorAuthController {

  private final TwoFactorAuthService twoFactorAuthService;

  @Autowired
  public TwoFactorAuthControllerImpl(TwoFactorAuthService twoFactorAuthService) {
    this.twoFactorAuthService = twoFactorAuthService;
  }

  @Override
  @PostMapping(value = "")
  public ResponseEntity<LoginResponseDto> login2FA(
      @RequestBody @Valid TwoFATokenDto twoFATokenDto) {
    twoFactorAuthService.login2FA();
    return ResponseEntity.ok().build();
  }

  @Override
  @PostMapping(value = "/secret")
  public ResponseEntity<TwoFASecretResponseDto> createSecret(
      @RequestBody @Valid TwoFASecretRequestDto twoFASecretRequestDto) {
    return ResponseEntity.ok(twoFactorAuthService.createSecret(twoFASecretRequestDto));
  }

  @Override
  @PostMapping(value = "/verify")
  public ResponseEntity<OTPWildcardResponseDto> verifySecretAndCreateWildcards(
      @RequestBody @Valid TwoFATokenDto twoFATokenDto) {
    return ResponseEntity.ok(twoFactorAuthService.createWildcards(twoFATokenDto));
  }
}
