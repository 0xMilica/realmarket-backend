package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.model.Token;

import java.util.Date;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;

public class TokenUtils {

  public static final Long TEST_EXPIRATION = 10000L;
  public static final String TEST_SECRET = "TEST_SECRET";
  public static final String TEST_JWT = "TEST_JWT";

  public static final Token TEST_TOKEN =
      Token.builder().auth(TEST_AUTH).expirationTime(new Date()).jwt(TEST_JWT).build();
}
