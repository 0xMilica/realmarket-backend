package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.JWT;

public interface JWTService {
  String createToken(Auth auth);

  JWT validateJWTOrThrowException(String jti);

  void deleteExpiredTokens();

  void prolongExpirationTime(JWT jwt);

  void deleteAllByAuth(Auth auth);

  void deleteAllByAuthAndValueNot(Auth auth, String value);

  void deleteByValue(final String token);
}
