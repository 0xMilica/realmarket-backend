package io.realmarket.propeler.service;

import io.realmarket.propeler.model.AuthState;
import io.realmarket.propeler.model.enums.AuthStateName;

public interface AuthStateService {
  AuthState getAuthState(AuthStateName authStateName);
}
