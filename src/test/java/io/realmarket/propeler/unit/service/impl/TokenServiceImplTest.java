package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.model.Token;
import io.realmarket.propeler.repository.TokenRepository;
import io.realmarket.propeler.service.JWTService;
import io.realmarket.propeler.service.impl.TokenServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.unit.util.TokenUtils.TEST_JWT;
import static io.realmarket.propeler.unit.util.TokenUtils.TEST_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class TokenServiceImplTest {

  @Mock private TokenRepository tokenRepository;
  @Mock private JWTService jwtService;

  @InjectMocks private TokenServiceImpl tokenService;

  @Test
  public void FindByJwtOrThrowException_Should_ReturnToken_IfJwtExists() {
    when(tokenRepository.findByJwt(TEST_JWT)).thenReturn(Optional.of(TEST_TOKEN));

    Token retVal = tokenService.findByJwtOrThrowException(TEST_JWT);

    assertEquals(TEST_TOKEN, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FindByJwtOrThrowException_Should_ThrowException_IfJwtNotExists() {
    when(tokenRepository.findByJwt(TEST_JWT)).thenReturn(Optional.empty());

    tokenService.findByJwtOrThrowException(TEST_JWT);
  }

  @Test
  public void CreateToken_Should_ReturnToken() {
    when(tokenRepository.save(any(Token.class))).thenReturn(TEST_TOKEN);
    when(jwtService.generateJWT(TEST_AUTH)).thenReturn(TEST_JWT);

    Token retVal = tokenService.createToken(TEST_AUTH);

    assertEquals(TEST_TOKEN, retVal);
  }
}
