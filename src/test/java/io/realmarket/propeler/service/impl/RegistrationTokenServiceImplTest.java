package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.RegistrationToken;
import io.realmarket.propeler.repository.RegistrationTokenRepository;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Instant;
import java.util.Optional;

import static io.realmarket.propeler.util.RegistrationTokenUtils.TEST_REGISTRATION_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class RegistrationTokenServiceImplTest {

  @Mock private RegistrationTokenRepository registrationTokenRepository;

  @InjectMocks private RegistrationTokenServiceImpl registrationTokenService;

  @Test
  public void FindByValueAndNotExpiredOrThrowException_Should_ReturnValidToken() {
    when(registrationTokenRepository.findByValueAndExpirationTimeGreaterThanEqual(
            any(String.class), any(Instant.class)))
        .thenReturn(Optional.of(TEST_REGISTRATION_TOKEN));

    RegistrationToken retVal =
        registrationTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_REGISTRATION_TOKEN.getValue());

    assertEquals(TEST_REGISTRATION_TOKEN, retVal);
  }

  @Test(expected = InvalidTokenException.class)
  public void FindByValueAndNotExpiredOrThrowException_Should_ThrowException_IfTokenNotValid() {
    when(registrationTokenRepository.findByValueAndExpirationTimeGreaterThanEqual(
            any(String.class), any(Instant.class)))
        .thenReturn(Optional.empty());

    registrationTokenService.findByValueAndNotExpiredOrThrowException(
        TEST_REGISTRATION_TOKEN.getValue());
  }

  @Test
  public void DeleteToken_Should_DeleteToken() {
    doNothing().when(registrationTokenRepository).delete(TEST_REGISTRATION_TOKEN);

    registrationTokenService.deleteToken(TEST_REGISTRATION_TOKEN);
  }

  @Test
  public void DeleteExpiredTokens_Should_DeleteExpiredTokens() {
    registrationTokenService.deleteExpiredTokens();
    verify(registrationTokenRepository, Mockito.times(1)).deleteAllByExpirationTimeLessThan(any());
  }
}
