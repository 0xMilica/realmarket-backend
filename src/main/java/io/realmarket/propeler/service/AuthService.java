package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.RegistrationDto;
import io.realmarket.propeler.api.dto.ConfirmRegistrationDto;
import io.realmarket.propeler.model.Auth;

import java.util.Optional;

public interface AuthService {
  void register(RegistrationDto registrationDto);

  Auth findByUsernameOrThrowException(String username);

  Optional<Auth> findById(Long id);

  void confirmRegistration(ConfirmRegistrationDto confirmRegistrationDto);
}
