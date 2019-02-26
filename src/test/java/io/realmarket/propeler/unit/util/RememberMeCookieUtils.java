package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.model.RememberMeCookie;

public class RememberMeCookieUtils {

  public static final String TEST_VALUE = "TEST_VALUE";

  public static final RememberMeCookie TEST_RM_COOKIE =
      RememberMeCookie.builder().value(TEST_VALUE).build();
}
