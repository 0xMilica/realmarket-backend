package io.realmarket.propeler.security;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.service.impl.AuthServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthentication implements Authentication {

  private final Auth auth;
  private final String token;

  private boolean authenticated = true;

  public UserAuthentication(Auth auth, String token) {
    this.auth = auth;
    this.token = token;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthServiceImpl.getAuthorities(auth.getUserRole());
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return auth;
  }

  @Override
  public Object getPrincipal() {
    return auth.getUsername();
  }

  @Override
  public boolean isAuthenticated() {
    return authenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    this.authenticated = isAuthenticated;
  }

  @Override
  public String getName() {
    return auth.getUsername();
  }

  public String getToken() {
    return token;
  }
}
