package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;

public interface TwoFactorAuthService {
  LoginResponseDto login2FA(TwoFATokenDto twoFATokenDto);

  TwoFASecretResponseDto createSecret(TwoFASecretRequestDto twoFASecretRequestDto);

  OTPWildcardResponseDto createWildcards(TwoFASecretVerifyDto twoFASecretVerifyDto);
}
