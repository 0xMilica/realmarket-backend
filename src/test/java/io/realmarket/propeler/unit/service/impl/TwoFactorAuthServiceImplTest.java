package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.OTPWildcardResponseDto;
import io.realmarket.propeler.api.dto.TwoFASecretResponseDto;
import io.realmarket.propeler.api.dto.TwoFATokenDto;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.OTPService;
import io.realmarket.propeler.service.TemporaryTokenService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.impl.OTPServiceImpl;
import io.realmarket.propeler.service.impl.TwoFactorAuthServiceImpl;
import io.realmarket.propeler.unit.util.OTPUtils;
import io.realmarket.propeler.unit.util.TemporaryTokenUtils;
import io.realmarket.propeler.unit.util.TwoFactorAuthUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static io.realmarket.propeler.unit.util.OTPUtils.TEST_SECRET;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OTPServiceImpl.class)
public class TwoFactorAuthServiceImplTest {

  @Mock private OTPService otpService;
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
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_TOKEN);
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
        twoFactorAuthService.createWildcards(TwoFactorAuthUtils.TEST_TWO_FA_TOKEN);

    assertEquals(wildcardList, otpWildcardResponseDto.getWildcards());
    verify(temporaryTokenService, times(1))
        .deleteToken(TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void createWildcards_Should_Throw_On_WrongTokenType() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_TOKEN);
    twoFactorAuthService.createWildcards(TwoFactorAuthUtils.TEST_TWO_FA_TOKEN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWildcards_Should_Throw_On_NoCode() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN);
    twoFactorAuthService.createWildcards(
        new TwoFATokenDto(TemporaryTokenUtils.TEST_TEMPORARY_TOKEN.getValue(), null, null));
  }

  @Test(expected = ForbiddenOperationException.class)
  public void createWildcards_Should_Throw_On_WrongCode() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(any()))
        .thenReturn(TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN);
    when(otpService.validateTOTPSecretChange(any(),any())).thenReturn(false);

    twoFactorAuthService.createWildcards(TwoFactorAuthUtils.TEST_TWO_FA_TOKEN);
  }
}
