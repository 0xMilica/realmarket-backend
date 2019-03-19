package io.realmarket.propeler.service.util;

import io.realmarket.propeler.model.RememberMeCookie;
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RememberMeCookieHelper {

  public static final String COOKIE_NAME = "remember-me";
  public static final String COOKIE_PATH = "/api/auth";

  public static final Integer MAX_AGE = 2592000;

  private RememberMeCookieHelper() {}

  public static Cookie getCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();

    if (ArrayUtils.isEmpty(cookies)) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (COOKIE_NAME.equals(cookie.getName())) {
        return cookie;
      }
    }
    return null;
  }

  public static String getCookieValue(HttpServletRequest request) {
    Cookie cookie = getCookie(request);
    return cookie == null ? null : cookie.getValue();
  }

  public static void deleteRememberMeCookie(Cookie cookie, HttpServletResponse response) {
    cookie.setMaxAge(0);
    cookie.setPath(COOKIE_PATH);
    response.addCookie(cookie);
  }

  public static void setRememberMeCookie(
      RememberMeCookie rememberMeCookie, HttpServletResponse response) {
    Cookie cookie = new Cookie(COOKIE_NAME, rememberMeCookie.getValue());
    cookie.setMaxAge(MAX_AGE);
    cookie.setPath(COOKIE_PATH);
    response.addCookie(cookie);
  }
}
