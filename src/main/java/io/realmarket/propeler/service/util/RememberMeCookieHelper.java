package io.realmarket.propeler.service.util;

import io.realmarket.propeler.model.RememberMeCookie;
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RememberMeCookieHelper {

  public static final String COOKIE_NAME = "remember-me";

  private RememberMeCookieHelper() {}

  public static String getCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();

    if (ArrayUtils.isEmpty(cookies)) {
      return null;
    }

    for (Cookie cookie : cookies) {
      if (COOKIE_NAME.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }

    return null;
  }

  public static void setRememberMeCookie(
      RememberMeCookie rememberMeCookie, HttpServletResponse response) {
    Cookie cookie = new Cookie(COOKIE_NAME, rememberMeCookie.getValue());
    cookie.setMaxAge(2000000);
    cookie.setPath("/api/auth");
    response.addCookie(cookie);
  }
}
