package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;

public interface TemporaryTokenService {

  TemporaryToken findByValueAndNotExpiredOrThrowException(String value);

  TemporaryToken createToken(Auth auth, ETemporaryTokenType type);

  TemporaryToken findByValueAndTypeOrThrowException(String value, ETemporaryTokenType type);

  void deleteToken(TemporaryToken temporaryToken);
}
