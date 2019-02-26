package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.TwoFactorAuthController;
import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.TwoFactorAuthService;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
  @PostMapping
  public ResponseEntity<LoginResponseDto> login2FA(
      @RequestBody @Valid LoginTwoFADto loginTwoFADto, HttpServletResponse response) {
    return ResponseEntity.ok(twoFactorAuthService.login2FA(loginTwoFADto, response));
  }

  @Override
  @PostMapping(value = "/remember_me")
  public ResponseEntity<LoginResponseDto> loginRememberMe(
      @RequestBody @Valid LoginTwoFADto loginTwoFADto, HttpServletRequest request) {
    return ResponseEntity.ok(twoFactorAuthService.loginRememberMe(loginTwoFADto, request));
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
      @RequestBody @Valid TwoFASecretVerifyDto twoFASecretVerifyDto) {
    return ResponseEntity.ok(twoFactorAuthService.createWildcards(twoFASecretVerifyDto));
  }
}
