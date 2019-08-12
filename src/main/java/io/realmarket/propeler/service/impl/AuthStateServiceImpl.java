package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.AuthState;
import io.realmarket.propeler.model.enums.AuthStateName;
import io.realmarket.propeler.repository.AuthStateRepository;
import io.realmarket.propeler.service.AuthStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AuthStateServiceImpl implements AuthStateService {

  private final AuthStateRepository authStateRepository;

  @Autowired
  public AuthStateServiceImpl(AuthStateRepository authStateRepository) {
    this.authStateRepository = authStateRepository;
  }

  @Override
  public AuthState getAuthState(AuthStateName authStateName) {
    return authStateRepository.findByName(authStateName).orElseThrow(EntityNotFoundException::new);
  }
}
