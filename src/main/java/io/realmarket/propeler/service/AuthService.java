package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.RegistrationDto;
import io.realmarket.propeler.model.Auth;

public interface AuthService {
  void register(RegistrationDto registrationDto);

  Auth findByUsernameOrThrowException(String username);
}
