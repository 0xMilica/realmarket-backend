package io.realmarket.propeler.service.impl;

import com.auth0.jwt.algorithms.Algorithm;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.JWT;
import io.realmarket.propeler.repository.JWTRepository;
import io.realmarket.propeler.service.JWTService;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {
  private static final String ISSUER = "realmarket.io";
  private final JWTRepository jwtRepository;

  @Value("${hmac.secret}")
  private String hmacSecret;

  @Value("${access-token.expiration-time}")
  private long tokenExpirationTime;

  @Autowired
  public JWTServiceImpl(JWTRepository jwtRepository) {
    this.jwtRepository = jwtRepository;
  }

  private String generateJWT(Auth auth) {
    return com.auth0
        .jwt
        .JWT
        .create()
        .withIssuer(ISSUER)
        .withSubject(String.valueOf(auth.getId()))
        .withIssuedAt(new Date())
        .sign(Algorithm.HMAC512(hmacSecret));
  }

  public JWT findByValueAndNotExpiredOrThrowException(String value) {
    return jwtRepository
        .findByValueAndExpirationTimeGreaterThanEqual(value, new Date())
        .orElseThrow(() -> new InvalidTokenException("Invalid token provided"));
  }

  public JWT createToken(Auth auth) {
    JWT jwtToken =
        JWT.builder()
            .auth(auth)
            .value(generateJWT(auth))
            .expirationTime(new Date(System.currentTimeMillis() + tokenExpirationTime))
            .build();

    return jwtRepository.save(jwtToken);
  }

  @Transactional
  @Scheduled(
      fixedRateString = "${app.cleanse.tokens.timeloop}",
      initialDelayString = "${app.cleanse.tokens.timeloop}")
  public void deleteExpiredTokens() {
    log.trace("Clean expired JWT");
    jwtRepository.deleteAllByExpirationTimeLessThan(new Date());
  }

  public void prolongExpirationTime(JWT jwt) {
    jwt.setExpirationTime(new Date(System.currentTimeMillis() + tokenExpirationTime));
    jwtRepository.save(jwt);
  }
}
