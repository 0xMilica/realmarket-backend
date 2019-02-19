package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.OTPWildcardResponseDto;
import io.realmarket.propeler.api.dto.TwoFASecretRequestDto;
import io.realmarket.propeler.api.dto.TwoFASecretResponseDto;
import io.realmarket.propeler.api.dto.TwoFATokenDto;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.OTPService;
import io.realmarket.propeler.service.TemporaryTokenService;
import io.realmarket.propeler.service.TwoFactorAuthService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

  private OTPService otpService;
  private AuthService authService;
  private TemporaryTokenService temporaryTokenService;

  @Autowired
  TwoFactorAuthServiceImpl(
      OTPService otpService, AuthService authService, TemporaryTokenService temporaryTokenService) {
    this.otpService = otpService;
    this.authService = authService;
    this.temporaryTokenService = temporaryTokenService;
  }

  public Boolean login2FA() {
    return true;
  }

  @Transactional
  public TwoFASecretResponseDto createSecret(TwoFASecretRequestDto twoFASecretRequestDto) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            twoFASecretRequestDto.getSetupToken());
    if (!temporaryToken.getTemporaryTokenType().equals(ETemporaryTokenType.SETUP_2FA_TOKEN)) {
      throw new ForbiddenOperationException(ExceptionMessages.INVALID_TOKEN_TYPE);
    }
    String secret = otpService.generateTOTPSecret(temporaryToken.getAuth());
    return TwoFASecretResponseDto.builder().secret(secret).token(temporaryToken.getValue()).build();
  }

  @Transactional
  public OTPWildcardResponseDto createWildcards(TwoFATokenDto twoFATokenDto) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(twoFATokenDto.getToken());
    if (temporaryToken.getTemporaryTokenType() != ETemporaryTokenType.SETUP_2FA_TOKEN) {
      throw new ForbiddenOperationException(ExceptionMessages.INVALID_TOKEN_TYPE);
    }
    if (twoFATokenDto.getCode() == null) {
      throw new IllegalArgumentException(ExceptionMessages.TOTP_CODE_NOT_PROVIDED);
    }
    if (!otpService.validateTOTPSecretChange(temporaryToken.getAuth(), twoFATokenDto.getCode())) {
      throw new ForbiddenOperationException(ExceptionMessages.INVALID_TOTP_CODE_PROVIDED);
    }
    List<String> wildcards = otpService.generateRecoveryCodes(temporaryToken.getAuth());
    temporaryTokenService.deleteToken(temporaryToken);
    return OTPWildcardResponseDto.builder().wildcards(wildcards).build();
  }
}
