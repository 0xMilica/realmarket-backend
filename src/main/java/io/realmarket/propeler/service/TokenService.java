package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Token;

public interface TokenService {

  Token findByJwtOrThrowException(String jwt);

  Token createToken(Auth auth);
}
