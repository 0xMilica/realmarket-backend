package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;

public interface AuthService {

  Auth findByUsernameOrThrowException(String username);
}
