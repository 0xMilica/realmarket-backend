package io.realmarket.propeler.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginIPAttemptsService extends LoginAttemptsServiceAbstract {
  public LoginIPAttemptsService(
      @Value("${app.login.ip.ban.time-banned}") final int banExpirationPeriod,
      @Value("${app.login.ip.ban.attempts}") final int maxAttempts) {
    super(banExpirationPeriod, maxAttempts);
  }
}
