package io.realmarket.propeler.service.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginAttemptsService {

  @Value("${app.login.ban.attempts}")
  private int maxAttempts;

  private LoadingCache<String, Integer> attemptsCache;

  public LoginAttemptsService(
      @Value("${app.login.ban.time-banned}") final int banExpirationPeriod) {
    super();
    attemptsCache =
        CacheBuilder.newBuilder()
            .expireAfterWrite(banExpirationPeriod, TimeUnit.MINUTES)
            .build(
                new CacheLoader<String, Integer>() {
                  public Integer load(String key) {
                    return 0;
                  }
                });
  }

  public void loginSucceeded(String key) {
    log.info("Login success : ip to invalidate {}", key);
    attemptsCache.invalidate(key);
  }

  public void loginFailed(String key) {
    int attempts;
    try {
      attempts = attemptsCache.get(key);
    } catch (ExecutionException e) {
      attempts = 0;
    }
    attemptsCache.put(key, ++attempts);
    log.info("Login failure : ip {}  wrong attempts {}", key, attempts);
  }

  public boolean isBlocked(String key) {
    try {
      return attemptsCache.get(key) >= maxAttempts;
    } catch (ExecutionException e) {
      return false;
    }
  }
}
