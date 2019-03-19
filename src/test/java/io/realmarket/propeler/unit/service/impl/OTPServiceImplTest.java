package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.OTPWildcard;
import io.realmarket.propeler.repository.AuthorizedActionRepository;
import io.realmarket.propeler.repository.OTPWildcardRepository;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.AuthorizedActionService;
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

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.unit.util.AuthorizedActionUtils.TEST_AUTHORIZED_ACTION;
import static io.realmarket.propeler.unit.util.OTPUtils.TEST_OTP_WILDCARD_1;
import static io.realmarket.propeler.unit.util.OTPUtils.TEST_TOTP_CODE_1;
import static io.realmarket.propeler.unit.util.TemporaryTokenUtils.TEST_SECRET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

  @Mock private AuthorizedActionService authorizedActionService;

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

    Boolean isValid = otpService.validate(TEST_AUTH, new TwoFADto(TEST_TOTP_CODE_1, null));
    assertEquals(isValid, false);
  }

  @Test
  public void Validate_Should_UnvalidateSecret() throws Exception {
    OTPServiceImpl otpServiceSpy = PowerMockito.spy(otpService);
    PowerMockito.doReturn(false).when(otpServiceSpy, "validateCode", anyString(), anyString());

    Boolean isValid = otpServiceSpy.validate(TEST_AUTH, new TwoFADto(TEST_TOTP_CODE_1, null));
    assertEquals(isValid, false);
  }

  @Test
  public void Validate_Should_ValidateRecoveryCode() throws Exception {
    when(otpWildcardRepository.findAllByAuth(any())).thenReturn(OTPUtils.TEST_OTP_WILDCARD_LIST());
    when(passwordEncoder.matches(TEST_OTP_WILDCARD_1, TEST_OTP_WILDCARD_1)).thenReturn(true);

    Boolean isValid = otpService.validate(TEST_AUTH, new TwoFADto(null, TEST_OTP_WILDCARD_1));
    assertEquals(isValid, true);
    OTPWildcard otpWildcard = OTPUtils.TEST_OTP_WILDCARD_1();
    verify(otpWildcardRepository, times(1)).deleteById(otpWildcard.getId());
  }

  @Test
  public void Validate_Should_UnvalidatedRecoveryCode() throws Exception {
    when(otpWildcardRepository.findAllByAuth(any())).thenReturn(OTPUtils.TEST_OTP_WILDCARD_LIST());
    when(passwordEncoder.matches(TEST_OTP_WILDCARD_1, TEST_OTP_WILDCARD_1)).thenReturn(false);

    Boolean isValid = otpService.validate(TEST_AUTH, new TwoFADto(null, TEST_OTP_WILDCARD_1));
    assertEquals(isValid, false);
  }

  @Test
  public void GenerateSecret_Should_ReturnSecret() {
    doNothing()
        .when(authorizedActionService)
        .storeAuthorizationAction(anyLong(), any(), any(), anyLong());

    String secret = otpService.generateTOTPSecret(TEST_AUTH);

    assertNotNull(secret);
  }

  @Test
  public void GenerateRecoveryCode_Should_ReturnRecoveryCode() {

    otpService.generateRecoveryCodes(TEST_AUTH);

    verify(otpWildcardRepository, times(1)).deleteAllByAuth(TEST_AUTH);
    verify(otpWildcardRepository, times(10)).save(any());
  }

  @Test
  public void ValidateTOTPSecretChange_Should_UpdateSecret() throws Exception {
    when(authorizedActionService.findAuthorizedActionOrThrowException(any(), any()))
        .thenReturn(TEST_AUTHORIZED_ACTION);
    otpService = PowerMockito.spy(otpService);
    doReturn(true).when(otpService, "validateCode", anyString(), anyString());

    Boolean ret = otpService.validateTOTPSecretChange(TEST_AUTH, TEST_SECRET);

    assertEquals(true, ret);
    verify(authService, times(1)).updateSecretById(TEST_AUTH.getId(), TEST_SECRET);
  }

  @Test
  public void ValidateTOTPSecretChange_Should_NotUpdateSecret_OnInvalidCode() throws Exception {
    when(authorizedActionService.findAuthorizedActionOrThrowException(any(), any()))
        .thenReturn(TEST_AUTHORIZED_ACTION);
    otpService = PowerMockito.spy(otpService);
    doReturn(false).when(otpService, "validateCode", anyString(), anyString());

    Boolean ret = otpService.validateTOTPSecretChange(TEST_AUTH, TEST_TOTP_CODE_1);
    assertEquals(false, ret);
  }
}
