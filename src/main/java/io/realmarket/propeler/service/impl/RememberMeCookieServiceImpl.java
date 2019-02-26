package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.RememberMeCookie;
import io.realmarket.propeler.repository.RememberMeCookieRepository;
import io.realmarket.propeler.service.RememberMeCookieService;
import io.realmarket.propeler.service.util.RandomStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class RememberMeCookieServiceImpl implements RememberMeCookieService {

  private static final int COOKIE_LENGTH = 54;

  @Value("${app.remember_me.expiration}")
  private long expirationPeriod;

  private final RememberMeCookieRepository rememberMeCookieRepository;

  @Autowired
  public RememberMeCookieServiceImpl(RememberMeCookieRepository rememberMeCookieRepository) {
    this.rememberMeCookieRepository = rememberMeCookieRepository;
  }

  public Optional<RememberMeCookie> findByValueAndNotExpired(String value) {
    return rememberMeCookieRepository.findByValueAndExpirationTimeGreaterThanEqual(
        value, Instant.now());
  }

  public RememberMeCookie createCookie(Auth auth) {
    return rememberMeCookieRepository.save(
        RememberMeCookie.builder()
            .value(RandomStringBuilder.generateBase32String(COOKIE_LENGTH))
            .auth(auth)
            .expirationTime(Instant.now().plusMillis(expirationPeriod))
            .build());
  }

  public void deleteCookie(RememberMeCookie rememberMeCookie) {
    rememberMeCookieRepository.delete(rememberMeCookie);
  }
}
