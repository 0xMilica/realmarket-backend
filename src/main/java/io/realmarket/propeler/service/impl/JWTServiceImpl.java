package io.realmarket.propeler.service.impl;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.JWT;
import io.realmarket.propeler.repository.JWTRepository;
import io.realmarket.propeler.service.JWTService;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.RandomStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {
  private static final String ISSUER = "realmarket.io";
  private final JWTRepository jwtRepository;

  private String hmacSecret;

  @Value("${access-token.expiration-time}")
  private long tokenExpirationTime;

  private JWTVerifier verifier;

  @Autowired
  public JWTServiceImpl(
      JWTRepository jwtRepository, @Value("${hmac.secret}") final String hmacSecret) {
    this.jwtRepository = jwtRepository;
    Algorithm algorithm = Algorithm.HMAC512(hmacSecret);
    verifier = com.auth0.jwt.JWT.require(algorithm).withIssuer(ISSUER).build();
    this.hmacSecret = hmacSecret;
  }

  @Override
  public JWT validateJWTOrThrowException(String jwt) {
    return jwtRepository
        .findByValueAndExpirationTimeGreaterThanEqual(verifier.verify(jwt).getId(), Instant.now())
        .orElseThrow(() -> new InvalidTokenException(ExceptionMessages.INVALID_TOKEN_PROVIDED));
  }

  @Override
  public String createToken(Auth auth) {
    String jti = RandomStringBuilder.generateBase32String(32);
    String jwt =
        com.auth0
            .jwt
            .JWT
            .create()
            .withIssuer(ISSUER)
            .withSubject(String.valueOf(auth.getId()))
            .withClaim("username", auth.getUsername())
            .withClaim("role", auth.getUserRole().toString())
            .withIssuedAt(Date.from(Instant.now()))
            .withJWTId(jti)
            .sign(Algorithm.HMAC512(hmacSecret));

    jwtRepository.save(
        JWT.builder()
            .auth(auth)
            .value(jti)
            .expirationTime(Instant.now().plusMillis(tokenExpirationTime))
            .build());
    return jwt;
  }

  @Transactional
  @Scheduled(
      fixedRateString = "${app.cleanse.tokens.timeloop}",
      initialDelayString = "${app.cleanse.tokens.timeloop}")
  public void deleteExpiredTokens() {
    log.trace("Clean expired JWT");
    jwtRepository.deleteAllByExpirationTimeLessThan(Instant.now());
  }

  public void prolongExpirationTime(JWT jwt) {
    jwt.setExpirationTime(Instant.now().plusMillis(tokenExpirationTime));
    jwtRepository.save(jwt);
  }

  public void deleteAllByAuth(Auth auth) {
    jwtRepository.deleteAllByAuth(auth);
  }

  public void deleteAllByAuthAndValueNot(Auth auth, String value) {
    jwtRepository.deleteAllByAuthAndValueNot(auth, value);
  }

  public void deleteByValue(final String token) {
    jwtRepository.deleteByValue(token);
  }
}
