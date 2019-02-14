package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.OTPService;
import io.realmarket.propeler.service.TwoFactorAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

  private OTPService otpService;
  private AuthService authService;

  @Autowired
  TwoFactorAuthServiceImpl(OTPService otpService, AuthService authService) {
    this.otpService = otpService;
    this.authService = authService;
  }

  public Boolean login() {
    return true;
  }

  public void createSecret() {}

  public void createRecoveryCodes() {}
}
