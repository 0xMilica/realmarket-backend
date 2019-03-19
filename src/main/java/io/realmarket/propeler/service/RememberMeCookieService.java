package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.RememberMeCookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface RememberMeCookieService {

  Optional<RememberMeCookie> findByValueAndAuthAndNotExpired(String value, Auth auth);

  RememberMeCookie createCookie(Auth auth);

  void deleteCookie(RememberMeCookie rememberMeCookie);

  void deleteCurrentCookie(HttpServletRequest request, HttpServletResponse response);
}
