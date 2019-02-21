package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

  private OTPService otpService;
  private JWTService jwtService;
  private AuthService authService;
  private TemporaryTokenService temporaryTokenService;

  @Autowired
  TwoFactorAuthServiceImpl(
      OTPService otpService,
      JWTService jwtService,
      AuthService authService,
      TemporaryTokenService temporaryTokenService) {
    this.otpService = otpService;
    this.jwtService = jwtService;
    this.authService = authService;
    this.temporaryTokenService = temporaryTokenService;
  }

  @Transactional
  public LoginResponseDto login2FA(TwoFATokenDto twoFATokenDto) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(twoFATokenDto.getToken());

    if (temporaryToken.getTemporaryTokenType() != ETemporaryTokenType.LOGIN_TOKEN) {
      throw new ForbiddenOperationException(ExceptionMessages.FORBIDDEN_OPERATION_EXCEPTION);
    }

    if (!otpService.validate(
        temporaryToken.getAuth(),
        new TwoFADto(twoFATokenDto.getCode(), twoFATokenDto.getWildcard()))) {
      throw new ForbiddenOperationException("Provided code not valid!");
    }

    temporaryTokenService.deleteToken(temporaryToken);

    return new LoginResponseDto(jwtService.createToken(temporaryToken.getAuth()).getValue());
  }

  @Override
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

  @Override
  @Transactional
  public OTPWildcardResponseDto createWildcards(TwoFASecretVerifyDto twoFASecretVerifyDto) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            twoFASecretVerifyDto.getToken());
    if (temporaryToken.getTemporaryTokenType() != ETemporaryTokenType.SETUP_2FA_TOKEN) {
      throw new ForbiddenOperationException(ExceptionMessages.INVALID_TOKEN_TYPE);
    }
    Auth auth = temporaryToken.getAuth();
    if (!otpService.validateTOTPSecretChange(auth, twoFASecretVerifyDto.getCode())) {
      throw new ForbiddenOperationException(ExceptionMessages.INVALID_TOTP_CODE_PROVIDED);
    }
    List<String> wildcards = otpService.generateRecoveryCodes(temporaryToken.getAuth());
    temporaryTokenService.deleteToken(temporaryToken);
    authService.finalize2faInitialization(auth);
    return OTPWildcardResponseDto.builder().wildcards(wildcards).build();
  }

  public SecretDto generateNewSecret(GenerateNewSecretDto generateNewSecretDto, Long userId) {
    Auth auth = authService.findByUserIdrThrowException(userId);

    authService.checkLoginCredentials(auth, generateNewSecretDto.getPassword());

    if (!otpService.validate(auth, generateNewSecretDto.getTwoFa())) {
      throw new BadCredentialsException(ExceptionMessages.INVALID_TOTP_CODE_PROVIDED);
    }
    String secret = otpService.generateTOTPSecret(auth);
    return new SecretDto(secret);
  }
}
