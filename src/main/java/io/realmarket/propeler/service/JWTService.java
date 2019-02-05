package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.JWT;

public interface JWTService {
  JWT createToken(Auth auth);

  JWT findByValueAndNotExpiredOrThrowException(String value);

  void deleteExpiredTokens();

  void prolongExpirationTime(JWT jwt);
}
