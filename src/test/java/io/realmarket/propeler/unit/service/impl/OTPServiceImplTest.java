package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.model.OTPWildcard;
import io.realmarket.propeler.repository.AuthorizedActionRepository;
import io.realmarket.propeler.repository.OTPWildcardRepository;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.impl.OTPServiceImpl;
import io.realmarket.propeler.unit.util.OTPUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.model.enums.EAuthorizationActionType.AUTH_ACTION_NEW_EMAIL;
import static io.realmarket.propeler.model.enums.EAuthorizationActionType.AUTH_ACTION_NEW_TOTP_SECRET;
import static io.realmarket.propeler.unit.util.AuthUtils.*;
import static io.realmarket.propeler.unit.util.OTPUtils.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OTPServiceImpl.class)
public class OTPServiceImplTest {
  @Mock private AuthorizedActionRepository authorizedActionRepository;

  @Mock private AuthService authService;

  @Mock private OTPWildcardRepository otpWildcardRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private OTPServiceImpl otpService;

  @Before
  public void SetUp() {
    ReflectionTestUtils.setField(otpService, "otpSecretSize", 16);
    ReflectionTestUtils.setField(otpService, "otpWildcardSize", 12);
    ReflectionTestUtils.setField(otpService, "otpWildcardBatchSize", 10);
  }

  @Test
  public void Validate_Should_ValidateSecret() throws Exception {
    otpService = PowerMockito.spy(otpService);
    doReturn(false).when(otpService, "validateCode", anyString(), anyString());

    Boolean isValid = otpService.validate(TEST_AUTH, TEST_SECRET);
    assertEquals(isValid, false);
  }

  @Test
  public void Validate_Should_UnvalidateSecret() throws Exception {
    OTPServiceImpl otpServiceSpy = PowerMockito.spy(otpService);
    PowerMockito.doReturn(false).when(otpServiceSpy, "validateCode", anyString(), anyString());

    Boolean isValid = otpServiceSpy.validate(TEST_AUTH, TEST_TOTP_CODE_1);
    assertEquals(isValid, false);
  }

  @Test
  public void Validate_Should_ValidateRecoveryCode() throws Exception {
    when(otpWildcardRepository.findAllByAuth(any()))
        .thenReturn(OTPUtils.TEST_OTP_WILDCARD_LIST());
    when(passwordEncoder.matches(TEST_OTP_WILDCARD_1, TEST_OTP_WILDCARD_1)).thenReturn(true);

    Boolean isValid = otpService.validate(TEST_AUTH, TEST_OTP_WILDCARD_1);
    assertEquals(isValid, true);
    OTPWildcard otpWildcard = OTPUtils.TEST_OTP_WILDCARD_1();
    verify(otpWildcardRepository, times(1)).deleteById(otpWildcard.getId());
  }

  @Test
  public void Validate_Should_UnvalidatedRecoveryCode() throws Exception {
    when(otpWildcardRepository.findAllByAuth(any()))
        .thenReturn(OTPUtils.TEST_OTP_WILDCARD_LIST());
    when(passwordEncoder.matches(TEST_OTP_WILDCARD_1, TEST_OTP_WILDCARD_1)).thenReturn(false);

    Boolean isValid = otpService.validate(TEST_AUTH, TEST_OTP_WILDCARD_1);
    assertEquals(isValid, false);
  }

  @Test
  public void GenerateSecret_Should_ReturnSecret() {

    String secret = otpService.generateTOTPSecret(TEST_AUTH);
    assertNotNull(secret);
    verify(authorizedActionRepository).save(any());
  }

  @Test
  public void GenerateRecoveryCode_Should_ReturnRecoveryCode() {

    otpService.generateRecoveryCodes(TEST_AUTH);

    verify(otpWildcardRepository, times(1)).deleteAllByAuth(TEST_AUTH);
    verify(otpWildcardRepository, times(10)).save(any());
  }

  @Test
  public void ValidateTOTPSecretChange_Should_UpdateSecret() throws Exception {
    when(authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(any(), any(),any()))
        .thenReturn(Optional.of(OTPUtils.TEST_AUTH_ACTION_NEW2FA()));
    otpService = PowerMockito.spy(otpService);
    doReturn(true).when(otpService, "validateCode", anyString(), anyString());

    Boolean ret = otpService.validateTOTPSecretChange(TEST_AUTH, TEST_TOTP_CODE_1);

    assertEquals(true, ret);
    verify(authService, times(1)).updateSecretById(TEST_AUTH.getId(), TEST_SECRET);
  }

  @Test
  public void ValidateTOTPSecretChange_Should_NotUpdateSecret_OnInvalidCode() throws Exception {
    when(authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(any(), any(),any()))
        .thenReturn(Optional.of(OTPUtils.TEST_AUTH_ACTION_NEW2FA()));
    otpService = PowerMockito.spy(otpService);
    doReturn(false).when(otpService, "validateCode", anyString(), anyString());

    Boolean ret = otpService.validateTOTPSecretChange(TEST_AUTH, TEST_TOTP_CODE_1);
    assertEquals(false, ret);
  }

  @Test(expected = EntityNotFoundException.class)
  public void validateTOTPSecretChange_Should_ThrowException_OnExpiredChange() throws Exception {
    when(authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(any(), any(), any()))
        .thenReturn(Optional.empty());

    otpService.validateTOTPSecretChange(TEST_AUTH, TEST_TOTP_CODE_1);
  }

  @Test(expected = EntityNotFoundException.class)
  public void ValidateTOTPSecretChange_Should_ThrowException_OnNoAuthorizationAction() throws Exception {
    when(authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(any(), any(), any())).thenReturn(Optional.empty());
    otpService.validateTOTPSecretChange(TEST_AUTH, TEST_TOTP_CODE_1);
  }

  @Test
  public void ValidateAuthorizedAction_Should_ReturnEmpty_On_NEW2FA_Check() {
    Optional<String> ret =
        otpService.validateAuthorizationAction(
            TEST_AUTH, AUTH_ACTION_NEW_TOTP_SECRET, TEST_TOTP_CODE_1);
    assertFalse(ret.isPresent());
  }

  @Test
  public void ValidateAuthorizationAction_Should_ValidateAndReturnSafedData() throws Exception {
    when(authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(any(), any(),any()))
        .thenReturn(Optional.of(OTPUtils.TEST_AUTH_ACTION_NEWEMAIL()));

    otpService = PowerMockito.spy(otpService);
    doReturn(true).when(otpService, "validateCode", anyString(), anyString());

    Optional<String> ret =
        otpService.validateAuthorizationAction(TEST_AUTH, AUTH_ACTION_NEW_EMAIL, TEST_TOTP_CODE_1);
    assertTrue(ret.isPresent());
    assertEquals(TEST_EMAIL, ret.get());
  }

  @Test
  public void ValidateAuthorizationAction_Should_ValidateAndReturnEmpty() throws Exception {

    when(authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(any(), any(),any()))
        .thenReturn(Optional.of(OTPUtils.TEST_AUTH_ACTION_NEWEMAIL()));

    otpService = PowerMockito.spy(otpService);
    doReturn(false).when(otpService, "validateCode", anyString(), anyString());

    Optional<String> ret =
        otpService.validateAuthorizationAction(TEST_AUTH, AUTH_ACTION_NEW_EMAIL, TEST_TOTP_CODE_1);
    assertFalse(ret.isPresent());
  }
}
