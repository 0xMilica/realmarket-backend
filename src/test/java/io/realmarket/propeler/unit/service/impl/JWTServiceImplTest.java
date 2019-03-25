package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.repository.JWTRepository;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import io.realmarket.propeler.service.impl.JWTServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.unit.util.JWTUtils.*;
import static io.realmarket.propeler.unit.util.TemporaryTokenUtils.TEST_EXPIRATION;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*"})
@PrepareForTest(JWTServiceImpl.class)
public class JWTServiceImplTest {

  @Mock private JWTRepository jwtRepository;
  private JWTServiceImpl jwtService;

  @Before
  public void initVerifier() {
    jwtService = new JWTServiceImpl(jwtRepository,TEST_HMAC_SECRET);
  }

  @Test
  public void GenerateJWT_Should_Return_JWT() {
    ReflectionTestUtils.setField(jwtService, "tokenExpirationTime", TEST_EXPIRATION);
    when(jwtRepository.save(any(io.realmarket.propeler.model.JWT.class))).thenReturn(TEST_JWT);

    String jwt = jwtService.createToken(TEST_AUTH);

    verify(jwtRepository, Mockito.times(1)).save(any(io.realmarket.propeler.model.JWT.class));
  }

  @Test
  public void FindByValueAndNotExpiredOrThrowException_Should_ReturnValidToken() throws Exception {

    when(jwtRepository.findByValueAndExpirationTimeGreaterThanEqual(
            any(String.class), any(Instant.class)))
        .thenReturn(Optional.of(TEST_JWT));

    io.realmarket.propeler.model.JWT retVal = jwtService.validateJWTOrThrowException(TEST_JWT_REAL);

    assertEquals(TEST_JWT, retVal);
  }

  @Test(expected = InvalidTokenException.class)
  public void FindByValueAndNotExpiredOrThrowException_Should_ThrowException_IfTokenNotValid() {

    when(jwtRepository.findByValueAndExpirationTimeGreaterThanEqual(
            any(String.class), any(Instant.class)))
        .thenReturn(Optional.empty());

    jwtService.validateJWTOrThrowException(TEST_JWT_REAL);
  }
}
