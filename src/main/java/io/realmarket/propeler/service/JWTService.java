package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;

public interface JWTService {

  String generateJWT(Auth auth);
}
