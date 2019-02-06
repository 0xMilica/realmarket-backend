package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.model.JWT;

import java.time.Instant;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;

public class JWTUtils {
  public static final String TEST_JWT_VALUE = "TEST_JWT_VALUE";

  public static final JWT TEST_JWT =
      JWT.builder().auth(TEST_AUTH).expirationTime(Instant.now()).value(TEST_JWT_VALUE).build();
}
