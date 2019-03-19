package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.service.util.RememberMeCookieHelper;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@Slf4j
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

  private final OTPService otpService;
  private final JWTService jwtService;
  private final AuthService authService;
  private final TemporaryTokenService temporaryTokenService;
  private final EmailService emailService;
  private final RememberMeCookieService rememberMeCookieService;

  @Autowired
  TwoFactorAuthServiceImpl(
      OTPService otpService,
      JWTService jwtService,
      AuthService authService,
      TemporaryTokenService temporaryTokenService,
      RememberMeCookieService rememberMeCookieService,
      EmailService emailService) {
    this.otpService = otpService;
    this.jwtService = jwtService;
    this.authService = authService;
    this.temporaryTokenService = temporaryTokenService;
    this.rememberMeCookieService = rememberMeCookieService;
    this.emailService = emailService;
  }

  @Transactional
  public LoginResponseDto login2FA(LoginTwoFADto loginTwoFADto, HttpServletResponse response) {
    TemporaryToken temporaryToken = findTemporaryLoginToken(loginTwoFADto.getToken());

    if (!otpService.validate(
        temporaryToken.getAuth(),
        new TwoFADto(loginTwoFADto.getCode(), loginTwoFADto.getWildcard()))) {
      throw new ForbiddenOperationException("Provided code not valid!");
    }

    if (loginTwoFADto.getRememberMe()) {
      RememberMeCookieHelper.setRememberMeCookie(
          rememberMeCookieService.createCookie(temporaryToken.getAuth()), response);
    }

    temporaryTokenService.deleteToken(temporaryToken);

    return new LoginResponseDto(jwtService.createToken(temporaryToken.getAuth()).getValue());
  }

  @Transactional
  public LoginResponseDto loginRememberMe(LoginTwoFADto loginTwoFADto, HttpServletRequest request) {
    TemporaryToken temporaryToken = findTemporaryLoginToken(loginTwoFADto.getToken());

    if (rememberMeCookieService
        .findByValueAndAuthAndNotExpired(
            RememberMeCookieHelper.getCookieValue(request), temporaryToken.getAuth())
        .isPresent()) {
      temporaryTokenService.deleteToken(temporaryToken);

      return new LoginResponseDto(jwtService.createToken(temporaryToken.getAuth()).getValue());
    } else {
      throw new ForbiddenOperationException("Invalid remember me cookie");
    }
  }

  private TemporaryToken findTemporaryLoginToken(String token) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(token);

    if (temporaryToken.getTemporaryTokenType() != ETemporaryTokenType.LOGIN_TOKEN) {
      throw new ForbiddenOperationException(ExceptionMessages.FORBIDDEN_OPERATION_EXCEPTION);
    }

    return temporaryToken;
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

  @Transactional
  public SecretDto generateNewSecret(TwoFATokenDto twoFATokenDto, Long userId) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndTypeOrThrowException(
            twoFATokenDto.getToken(), ETemporaryTokenType.PASSWORD_VERIFIED_TOKEN);
    Auth auth = authService.findByUserIdrThrowException(userId);

    if (!otpService.validate(
        auth, new TwoFADto(twoFATokenDto.getCode(), twoFATokenDto.getWildcard()))) {
      throw new BadCredentialsException(ExceptionMessages.INVALID_TOTP_CODE_PROVIDED);
    }
    String secret = otpService.generateTOTPSecret(auth);
    temporaryTokenService.deleteToken(temporaryToken);
    return new SecretDto(secret);
  }

  @Override
  @Transactional
  public OTPWildcardResponseDto createWildcards(Long authId, TwoFADto twoFADto) {
    Auth auth = authService.findByIdOrThrowException(authId);
    if (!otpService.validate(auth, twoFADto)) {
      throw new BadCredentialsException(ExceptionMessages.INVALID_TOTP_CODE_PROVIDED);
    }
    return new OTPWildcardResponseDto(otpService.generateRecoveryCodes(auth));
  }

  public void verifyNewSecret(VerifySecretChangeDto verifySecretChangeDto, Long userId) {
    Auth auth = authService.findByUserIdrThrowException(userId);
    if (!otpService.validateTOTPSecretChange(auth, verifySecretChangeDto.getCode())) {
      throw new BadCredentialsException(ExceptionMessages.INVALID_TOTP_CODE_PROVIDED);
    }
    emailService.sendMailToUser(
        new MailContentHolder(auth.getPerson().getEmail(), EEmailType.SECRET_CHANGE, null));
  }
}
