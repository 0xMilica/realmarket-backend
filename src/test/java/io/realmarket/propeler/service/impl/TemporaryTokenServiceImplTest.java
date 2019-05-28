package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.repository.TemporaryTokenRepository;
import io.realmarket.propeler.repository.TemporaryTokenTypeRepository;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Instant;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.util.TemporaryTokenUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class TemporaryTokenServiceImplTest {

  @Mock private TemporaryTokenRepository temporaryTokenRepository;
  @Mock private TemporaryTokenTypeRepository temporaryTokenTypeRepository;

  @InjectMocks private TemporaryTokenServiceImpl temporaryTokenService;

  @Test
  public void FindByValueAndNotExpiredOrThrowException_Should_ReturnValidToken() throws Exception {
    when(temporaryTokenRepository.findByValueAndExpirationTimeGreaterThanEqual(
            any(String.class), any(Instant.class)))
        .thenReturn(Optional.of(TEST_TEMPORARY_TOKEN));

    TemporaryToken retVal =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(TEST_TEMPORARY_TOKEN_VALUE);

    assertEquals(TEST_TEMPORARY_TOKEN, retVal);
  }

  @Test(expected = InvalidTokenException.class)
  public void FindByValueAndNotExpiredOrThrowException_Should_ThrowException_IfTokenNotValid() {
    when(temporaryTokenRepository.findByValueAndExpirationTimeGreaterThanEqual(
            any(String.class), any(Instant.class)))
        .thenReturn(Optional.empty());

    temporaryTokenService.findByValueAndNotExpiredOrThrowException(TEST_TEMPORARY_TOKEN_VALUE);
  }

  @Test
  public void CreateToken_Should_ReturnToken() {
    when(temporaryTokenRepository.save(any(TemporaryToken.class))).thenReturn(TEST_TEMPORARY_TOKEN);
    when(temporaryTokenTypeRepository.findByName(any()))
        .thenReturn(Optional.of(TEST_TEMPORARY_TOKEN_TYPE));

    TemporaryToken retVal =
        temporaryTokenService.createToken(TEST_AUTH, TEST_TEMPORARY_TOKEN_ENUM_TYPE);

    assertEquals(TEST_TEMPORARY_TOKEN, retVal);
  }

  @Test
  public void DeleteToken_Should_DeleteToken() {
    doNothing().when(temporaryTokenRepository).delete(TEST_TEMPORARY_TOKEN);

    temporaryTokenService.deleteToken(TEST_TEMPORARY_TOKEN);
  }

  @Test
  public void DeleteExpiredTokens_Should_DeleteExpiredTokens() {
    temporaryTokenService.deleteExpiredTokens();
    verify(temporaryTokenRepository, Mockito.times(1)).deleteAllByExpirationTimeLessThan(any());
  }
}
