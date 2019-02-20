package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.OTPWildcardResponseDto;
import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.api.dto.TwoFASecretResponseDto;
import io.realmarket.propeler.api.dto.TwoFASecretVerifyDto;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.JWTService;
import io.realmarket.propeler.service.OTPService;
import io.realmarket.propeler.service.TemporaryTokenService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.impl.OTPServiceImpl;
import io.realmarket.propeler.service.impl.TwoFactorAuthServiceImpl;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import io.realmarket.propeler.unit.util.OTPUtils;
import io.realmarket.propeler.unit.util.TemporaryTokenUtils;
import io.realmarket.propeler.unit.util.TwoFactorAuthUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.unit.util.JWTUtils.TEST_JWT;
import static io.realmarket.propeler.unit.util.OTPUtils.TEST_SECRET;
import static io.realmarket.propeler.unit.util.TemporaryTokenUtils.TEST_TEMPORARY_TOKEN;
import static io.realmarket.propeler.unit.util.TwoFactorAuthUtils.TEST_TWO_FA_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OTPServiceImpl.class)
public class TwoFactorAuthServiceImplTest {

  @Mock private OTPService otpService;
  @Mock private JWTService jwtService;
  @Mock private AuthService authService;
  @Mock private TemporaryTokenService temporaryTokenService;

  @InjectMocks private TwoFactorAuthServiceImpl twoFactorAuthService;

  @Test
  public void createSecret_Should_ReturnSecret() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN);

    when(otpService.generateTOTPSecret(any())).thenReturn(TEST_SECRET);

    TwoFASecretResponseDto twoFASecretResponseDto =
        twoFactorAuthService.createSecret(TwoFactorAuthUtils.TEST_2FA_SECRET_REQUEST);
    assertEquals(
        TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN.getValue(),
        twoFASecretResponseDto.getToken());
    assertEquals(TEST_SECRET, twoFASecretResponseDto.getSecret());
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

  @Test
  public void Login2FA_Should_Return_2FATokenDto() {
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
    when(jwtService.createToken(TEST_AUTH)).thenReturn(TEST_JWT);

    LoginResponseDto retVal = twoFactorAuthService.login2FA(TEST_TWO_FA_TOKEN);

    assertEquals(TEST_JWT.getValue(), retVal.getJwt());
  }

  @Test(expected = EntityNotFoundException.class)
  public void Login2FA_Should_Throw_NotFoundException_IfTokenNotExists() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_TWO_FA_TOKEN.getToken()))
        .thenThrow(EntityNotFoundException.class);

    twoFactorAuthService.login2FA(TEST_TWO_FA_TOKEN);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void Login2FA_Should_Return_ForbiddenOperationException_IfTokenNotValid() {
    TemporaryToken temporaryTokenMocked = TemporaryToken.builder().auth(TEST_AUTH).build();

    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_TWO_FA_TOKEN.getToken()))
        .thenReturn(temporaryTokenMocked);
    when(otpService.validate(
            TEST_AUTH, new TwoFADto(TEST_TWO_FA_TOKEN.getCode(), TEST_TWO_FA_TOKEN.getWildcard())))
        .thenReturn(true);

    twoFactorAuthService.login2FA(TEST_TWO_FA_TOKEN);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void Login2FA_Should_Return_ForbiddenOperationException_IfCodeNotValid() {
    TemporaryToken temporaryTokenMocked =
        TemporaryToken.builder()
            .temporaryTokenType(ETemporaryTokenType.LOGIN_TOKEN)
            .auth(TEST_AUTH)
            .build();

    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_TWO_FA_TOKEN.getToken()))
        .thenReturn(temporaryTokenMocked);
    when(otpService.validate(
            TEST_AUTH, new TwoFADto(TEST_TWO_FA_TOKEN.getCode(), TEST_TWO_FA_TOKEN.getWildcard())))
        .thenReturn(false);

    twoFactorAuthService.login2FA(TEST_TWO_FA_TOKEN);
  }
}
