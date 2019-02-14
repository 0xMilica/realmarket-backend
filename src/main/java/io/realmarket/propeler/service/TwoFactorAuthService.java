package io.realmarket.propeler.service;

public interface TwoFactorAuthService {
  Boolean login();

  void createSecret();

  void createRecoveryCodes();
}
