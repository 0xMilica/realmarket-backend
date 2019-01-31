package io.realmarket.propeler.unit.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.realmarket.propeler.service.impl.JWTServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.unit.util.TokenUtils.TEST_EXPIRATION;
import static io.realmarket.propeler.unit.util.TokenUtils.TEST_SECRET;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*"})
@PrepareForTest(JWTServiceImpl.class)
public class JWTServiceImplTest {
  @InjectMocks private JWTServiceImpl jwtService;

  @Test
  public void GenerateJWT_Should_Return_JWT() {
    ReflectionTestUtils.setField(jwtService, "hmacSecret", TEST_SECRET);
    ReflectionTestUtils.setField(jwtService, "tokenExpirationTime", TEST_EXPIRATION);

    String jwt = jwtService.generateJWT(TEST_AUTH);

    JWT.require(Algorithm.HMAC512(TEST_SECRET.getBytes())).build().verify(jwt);
    assertEquals("realmarket.io", JWT.decode(jwt).getIssuer());
  }
}
