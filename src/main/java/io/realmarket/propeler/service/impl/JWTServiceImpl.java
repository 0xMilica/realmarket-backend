package io.realmarket.propeler.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.service.JWTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService {
  private static final String ISSUER = "realmarket.io";

  @Value("${hmac.secret}")
  private String hmacSecret;

  @Value("${access-token.expiration-time}")
  private long tokenExpirationTime;

  public String generateJWT(Auth auth) {
    return JWT.create()
        .withIssuer(ISSUER)
        .withSubject(String.valueOf(auth.getId()))
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationTime))
        .sign(Algorithm.HMAC512(hmacSecret));
  }
}
