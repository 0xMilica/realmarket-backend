package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.*;

public interface TwoFactorAuthService {
  Boolean login2FA();

  TwoFASecretResponseDto createSecret(TwoFASecretRequestDto twoFASecretRequestDto);

  OTPWildcardResponseDto createWildcards(TwoFATokenDto twoFATokenDto);
}
