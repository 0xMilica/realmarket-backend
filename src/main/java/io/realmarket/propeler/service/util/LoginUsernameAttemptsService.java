package io.realmarket.propeler.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginUsernameAttemptsService extends LoginAttemptsServiceAbstract {
  public LoginUsernameAttemptsService(
      @Value("${app.login.username.ban.time-banned}") final int banExpirationPeriod,
      @Value("${app.login.username.ban.attempts}") final int maxAttempts) {
    super(banExpirationPeriod, maxAttempts);
  }
}
