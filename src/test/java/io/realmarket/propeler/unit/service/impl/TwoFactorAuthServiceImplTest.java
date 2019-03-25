package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.impl.OTPServiceImpl;
import io.realmarket.propeler.service.impl.TwoFactorAuthServiceImpl;
import io.realmarket.propeler.service.util.LoginAttemptsService;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import io.realmarket.propeler.unit.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.BadCredentialsException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.*;
import static io.realmarket.propeler.unit.util.JWTUtils.TEST_JWT;
import static io.realmarket.propeler.unit.util.JWTUtils.TEST_JWT_VALUE;
import static io.realmarket.propeler.unit.util.OTPUtils.TEST_SECRET_1;
import static io.realmarket.propeler.unit.util.RememberMeCookieUtils.TEST_RM_COOKIE;
import static io.realmarket.propeler.unit.util.RememberMeCookieUtils.TEST_VALUE;
import static io.realmarket.propeler.unit.util.TemporaryTokenUtils.TEST_TEMPORARY_TOKEN;
import static io.realmarket.propeler.unit.util.TwoFactorAuthUtils.LOGIN_2F_DTO_RM;
import static io.realmarket.propeler.unit.util.TwoFactorAuthUtils.TEST_TWO_FA_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OTPServiceImpl.class)
public class TwoFactorAuthServiceImplTest {

  @Mock private OTPService otpService;
  @Mock private JWTService jwtService;
  @Mock private AuthService authService;
  @Mock private RememberMeCookieService rememberMeCookieService;
  @Mock private LoginAttemptsService loginAttemptsService;
  @Mock private TemporaryTokenService temporaryTokenService;

  @InjectMocks private TwoFactorAuthServiceImpl twoFactorAuthService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void createSecret_Should_ReturnSecret() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN);

    when(otpService.generateTOTPSecret(any())).thenReturn(TEST_SECRET_1);

    TwoFASecretResponseDto twoFASecretResponseDto =
        twoFactorAuthService.createSecret(TwoFactorAuthUtils.TEST_2FA_SECRET_REQUEST);
    assertEquals(
        TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN.getValue(),
        twoFASecretResponseDto.getToken());
    assertEquals(TEST_SECRET_1, twoFASecretResponseDto.getSecret());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void createSecret_Should_Throw_On_WrongTokenType() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    twoFactorAuthService.createSecret(TwoFactorAuthUtils.TEST_2FA_SECRET_REQUEST);
  }

  @Test
  public void createWildcards_Should_ReturnWildcards() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN);

    List<String> wildcardList = OTPUtils.TEST_OTP_WILDCARD_STRING_LIST();

    when(otpService.validateTOTPSecretChange(any(), any())).thenReturn(true);
    when(otpService.generateRecoveryCodes(any())).thenReturn(wildcardList);

    OTPWildcardResponseDto otpWildcardResponseDto =
        twoFactorAuthService.createWildcards(TwoFactorAuthUtils.TEST_TWO_FA_SECRET_VERIFY_REQUEST);

    assertEquals(wildcardList, otpWildcardResponseDto.getWildcards());
    verify(temporaryTokenService, times(1))
        .deleteToken(TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void createWildcards_Should_Throw_On_WrongTokenType() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_TOKEN);
    twoFactorAuthService.createWildcards(TwoFactorAuthUtils.TEST_TWO_FA_SECRET_VERIFY_REQUEST);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void createWildcards_Should_Throw_On_NoCode() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN);
    twoFactorAuthService.createWildcards(
        new TwoFASecretVerifyDto(null, TemporaryTokenUtils.TEST_TEMPORARY_TOKEN.getValue()));
  }

  @Test(expected = ForbiddenOperationException.class)
  public void createWildcards_Should_Throw_On_WrongCode() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN);
    when(otpService.validateTOTPSecretChange(any(), any())).thenReturn(false);

    twoFactorAuthService.createWildcards(TwoFactorAuthUtils.TEST_TWO_FA_SECRET_VERIFY_REQUEST);
  }

  @Test(expected = BadCredentialsException.class)
  public void createWildcards2_Should_Throw_On_WrongCode() {
    when(authService.findByIdOrThrowException(any())).thenReturn(AuthUtils.TEST_AUTH);

    when(otpService.validate(any(), any())).thenReturn(false);

    twoFactorAuthService.createWildcards(AuthUtils.TEST_AUTH_ID, TwoFactorAuthUtils.TEST_2FA_DTO);
  }

  @Test(expected = BadCredentialsException.class)
  public void createWildcards2_Should_ReturnWildcards() {
    List<String> wildcardList = OTPUtils.TEST_OTP_WILDCARD_STRING_LIST();
    when(authService.findByIdOrThrowException(any())).thenReturn(AuthUtils.TEST_AUTH);
    when(otpService.validate(any(), any())).thenReturn(false);
    when(otpService.generateRecoveryCodes(any())).thenReturn(wildcardList);

    OTPWildcardResponseDto otpWildcardResponseDto =
        twoFactorAuthService.createWildcards(
            AuthUtils.TEST_AUTH_ID, TwoFactorAuthUtils.TEST_2FA_DTO);

    assertEquals(wildcardList, otpWildcardResponseDto);
  }

  @Test
  public void login2FA_Should_Return_LoginResponseDto() {
    TemporaryToken temporaryTokenMocked =
        TemporaryToken.builder()
            .temporaryTokenType(ETemporaryTokenType.LOGIN_TOKEN)
            .auth(TEST_AUTH)
            .value("TEST_VALUE")
            .build();

    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_TWO_FA_TOKEN.getToken()))
        .thenReturn(temporaryTokenMocked);
    when(otpService.validate(
            TEST_AUTH, new TwoFADto(TEST_TWO_FA_TOKEN.getCode(), TEST_TWO_FA_TOKEN.getWildcard())))
        .thenReturn(true);
    when(jwtService.createToken(TEST_AUTH)).thenReturn(TEST_JWT_VALUE);
    when(rememberMeCookieService.createCookie(TEST_AUTH)).thenReturn(TEST_RM_COOKIE);

    LoginResponseDto retVal = twoFactorAuthService.login2FA(LOGIN_2F_DTO_RM, TEST_RESPONSE);

    assertEquals(TEST_JWT.getValue(), retVal.getJwt());
    // TODO : assertEquals(TEST_RM_COOKIE.getValue(), TEST_RESPONSE.getCookie())
  }

  @Test(expected = EntityNotFoundException.class)
  public void login2FA_Should_Throw_NotFoundException_IfTokenNotExists() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_TWO_FA_TOKEN.getToken()))
        .thenThrow(EntityNotFoundException.class);

    twoFactorAuthService.login2FA(LOGIN_2F_DTO_RM, TEST_RESPONSE);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void login2FA_Should_Return_ForbiddenOperationException_IfTokenNotValid() {
    TemporaryToken temporaryTokenMocked = TemporaryToken.builder().auth(TEST_AUTH).build();

    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_TWO_FA_TOKEN.getToken()))
        .thenReturn(temporaryTokenMocked);
    when(otpService.validate(
            TEST_AUTH, new TwoFADto(TEST_TWO_FA_TOKEN.getCode(), TEST_TWO_FA_TOKEN.getWildcard())))
        .thenReturn(true);

    twoFactorAuthService.login2FA(LOGIN_2F_DTO_RM, TEST_RESPONSE);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void login2FA_Should_Return_ForbiddenOperationException_IfCodeNotValid() {
    TemporaryToken temporaryTokenMocked =
        TemporaryToken.builder()
            .temporaryTokenType(ETemporaryTokenType.LOGIN_TOKEN)
            .auth(TEST_AUTH)
            .build();

    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(LOGIN_2F_DTO_RM.getToken()))
        .thenReturn(temporaryTokenMocked);
    when(otpService.validate(
            TEST_AUTH, new TwoFADto(LOGIN_2F_DTO_RM.getCode(), LOGIN_2F_DTO_RM.getWildcard())))
        .thenReturn(false);

    twoFactorAuthService.login2FA(LOGIN_2F_DTO_RM, TEST_RESPONSE);
  }

  @Test
  public void loginRememberMe_Return_LoginResponseDto() {
    TemporaryToken temporaryTokenMocked =
        TemporaryToken.builder()
            .temporaryTokenType(ETemporaryTokenType.LOGIN_TOKEN)
            .auth(TEST_AUTH)
            .build();

    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(LOGIN_2F_DTO_RM.getToken()))
        .thenReturn(temporaryTokenMocked);
    when(rememberMeCookieService.findByValueAndAuthAndNotExpired(TEST_VALUE, TEST_AUTH))
        .thenReturn(Optional.of(TEST_RM_COOKIE));
    when(jwtService.createToken(TEST_AUTH)).thenReturn(TEST_JWT_VALUE);

    LoginResponseDto retVal = twoFactorAuthService.loginRememberMe(LOGIN_2F_DTO_RM, TEST_REQUEST);

    assertEquals(TEST_JWT.getValue(), retVal.getJwt());
  }

  @Test(expected = EntityNotFoundException.class)
  public void loginRememberMe_Should_Return_ForbiddenOperationException_IfTokenNotExists() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(LOGIN_2F_DTO_RM.getToken()))
        .thenThrow(EntityNotFoundException.class);
    twoFactorAuthService.loginRememberMe(LOGIN_2F_DTO_RM, TEST_REQUEST);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void loginRememberMe_Should_Return_ForbiddenOperationException_IfTokenNotValid() {
    TemporaryToken temporaryTokenMocked = TemporaryToken.builder().auth(TEST_AUTH).build();

    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_TWO_FA_TOKEN.getToken()))
        .thenReturn(temporaryTokenMocked);

    twoFactorAuthService.loginRememberMe(LOGIN_2F_DTO_RM, TEST_REQUEST);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void loginRememberMe_Should_Return_ForbiddenOperationException_IfCookieNotValid() {
    TemporaryToken temporaryTokenMocked =
        TemporaryToken.builder()
            .temporaryTokenType(ETemporaryTokenType.LOGIN_TOKEN)
            .auth(TEST_AUTH)
            .build();

    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(LOGIN_2F_DTO_RM.getToken()))
        .thenReturn(temporaryTokenMocked);
    when(rememberMeCookieService.findByValueAndAuthAndNotExpired(TEST_VALUE, TEST_AUTH))
        .thenReturn(Optional.empty());

    twoFactorAuthService.loginRememberMe(LOGIN_2F_DTO_RM, TEST_REQUEST);
  }

  @Test
  public void generateNewSecret_Should_Return_Codes() {
    Auth testAuth = AuthUtils.TEST_AUTH_OLD_SECRET;
    when(authService.findByUserIdrThrowException(PersonUtils.TEST_PERSON_ID)).thenReturn(testAuth);
    when(temporaryTokenService.findByValueAndTypeOrThrowException(any(), any()))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    when(otpService.validate(any(), any())).thenReturn(true);
    when(otpService.generateTOTPSecret(testAuth)).thenReturn(OTPUtils.TEST_SECRET_2);
    when(otpService.generateRecoveryCodes(testAuth))
        .thenReturn(OTPUtils.TEST_OTP_WILDCARD_STRING_LIST());

    SecretDto secretDto =
        twoFactorAuthService.generateNewSecret(
            TwoFactorAuthUtils.TEST_TWO_FA_TOKEN_1, PersonUtils.TEST_PERSON_ID);
    assertNotNull(secretDto.getSecret());
    verify(temporaryTokenService, times(1)).deleteToken(TEST_TEMPORARY_TOKEN);
  }

  @Test(expected = BadCredentialsException.class)
  public void generateNewSecret_Should_Throw_Exception_On_Wrong_2faCode() {
    Auth testAuth = AuthUtils.TEST_AUTH_OLD_SECRET;
    when(authService.findByUserIdrThrowException(PersonUtils.TEST_PERSON_ID)).thenReturn(testAuth);
    when(otpService.validate(any(), any())).thenReturn(false);

    twoFactorAuthService.generateNewSecret(
        TwoFactorAuthUtils.TEST_TWO_FA_TOKEN_1, PersonUtils.TEST_PERSON_ID);
  }
}
