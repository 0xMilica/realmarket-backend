package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AuthServiceImpl implements AuthService {

  private final AuthRepository authRepository;

  @Autowired
  public AuthServiceImpl(AuthRepository authRepository) {
    this.authRepository = authRepository;
  }

  public Auth findByUsernameOrThrowException(String username) {
    return authRepository
        .findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User with provided does not exists."));
  }
}
