package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TwoFactorAuthService {
  LoginResponseDto login2FA(LoginTwoFADto loginTwoFADto, HttpServletResponse response);

  LoginResponseDto loginRememberMe(LoginTwoFADto loginTwoFADto, HttpServletRequest request);

  TwoFASecretResponseDto createSecret(TwoFASecretRequestDto twoFASecretRequestDto);

  OTPWildcardResponseDto createWildcards(TwoFASecretVerifyDto twoFASecretVerifyDto);

  SecretDto generateNewSecret(GenerateNewSecretDto generateNewSecretDto, Long userId);

  OTPWildcardResponseDto createWildcards(Long authId, TwoFADto twoFADto);

  void verifyNewSecret(VerifySecretChangeDto verifySecretChangeDto, Long userId);
}
