package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.RememberMeCookie;

import java.util.Optional;

public interface RememberMeCookieService {

  Optional<RememberMeCookie> findByValueAndNotExpired(String value);

  RememberMeCookie createCookie(Auth auth);

  void deleteCookie(RememberMeCookie rememberMeCookie);
}
