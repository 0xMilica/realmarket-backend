package io.realmarket.propeler.service.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class LoginAttemptsServiceAbstract {

  protected int maxAttempts;

  protected LoadingCache<String, Integer> attemptsCache;

  protected LoginAttemptsServiceAbstract(final int banExpirationPeriod, final int maxAttempts) {
    attemptsCache =
        CacheBuilder.newBuilder()
            .expireAfterWrite(banExpirationPeriod, TimeUnit.MINUTES)
            .build(
                new CacheLoader<String, Integer>() {
                  public Integer load(String key) {
                    return 0;
                  }
                });
    this.maxAttempts = maxAttempts;
  }

  public void loginSucceeded(String key) {
    log.info("Login success : key to invalidate {}", key);
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
    log.info("Login failure : key {}  wrong attempts {}", key, attempts);
  }

  public boolean isBlocked(String key) {
    try {
      return attemptsCache.get(key) >= maxAttempts;
    } catch (ExecutionException e) {
      return false;
    }
  }
}
