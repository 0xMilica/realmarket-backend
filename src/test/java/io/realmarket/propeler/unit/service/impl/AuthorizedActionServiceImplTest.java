package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.repository.AuthorizedActionRepository;
import io.realmarket.propeler.service.AuthorizedActionService;
import io.realmarket.propeler.service.OTPService;
import io.realmarket.propeler.service.impl.AuthServiceImpl;
import io.realmarket.propeler.unit.util.OTPUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static io.realmarket.propeler.model.enums.EAuthorizationActionType.NEW_EMAIL;
import static io.realmarket.propeler.model.enums.EAuthorizationActionType.NEW_TOTP_SECRET;
import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.unit.util.AuthUtils.TEST_EMAIL;
import static io.realmarket.propeler.unit.util.TwoFactorAuthUtils.TEST_2FA_DTO;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthServiceImpl.class)
public class AuthorizedActionServiceImplTest {

  @Mock AuthorizedActionRepository authorizedActionRepository;
  @Mock OTPService otpService;

  @InjectMocks AuthorizedActionService authorizedActionService;

  @Test
  public void ValidateAuthorizedAction_Should_ReturnEmpty_On_NEW2FA_Check() {
    Optional<String> ret =
        authorizedActionService.validateAuthorizationAction(
            TEST_AUTH, NEW_TOTP_SECRET, TEST_2FA_DTO);
    assertFalse(ret.isPresent());
  }

  @Test
  public void ValidateAuthorizationAction_Should_ValidateAndReturnSafeData() throws Exception {
    when(authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(any(), any(), any()))
        .thenReturn(Optional.of(OTPUtils.TEST_AUTH_ACTION_NEWEMAIL()));


      when(otpService.validate(TEST_AUTH, TEST_2FA_DTO)).thenReturn(true);
    Optional<String> ret =
        authorizedActionService.validateAuthorizationAction(TEST_AUTH, NEW_EMAIL, TEST_2FA_DTO);
    assertTrue(ret.isPresent());
    assertEquals(TEST_EMAIL, ret.get());
  }

  @Test
  public void ValidateAuthorizationAction_Should_ValidateAndReturnEmpty() throws Exception {
    when(authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(any(), any(), any()))
        .thenReturn(Optional.of(OTPUtils.TEST_AUTH_ACTION_NEWEMAIL()));
    when(otpService.validate(TEST_AUTH, TEST_2FA_DTO)).thenReturn(false);

    Optional<String> ret =
        authorizedActionService.validateAuthorizationAction(TEST_AUTH, NEW_EMAIL, TEST_2FA_DTO);
    assertFalse(ret.isPresent());
  }
}
