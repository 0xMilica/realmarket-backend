package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.TemporaryTokenTypeName;

public interface TemporaryTokenService {

  TemporaryToken findByValueAndNotExpiredOrThrowException(String value);

  TemporaryToken createToken(Auth auth, TemporaryTokenTypeName type);

  TemporaryToken findByValueAndTypeOrThrowException(String value, TemporaryTokenTypeName type);

  void deleteToken(TemporaryToken temporaryToken);
}
