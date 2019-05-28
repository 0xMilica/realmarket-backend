package io.realmarket.propeler.util;

import io.realmarket.propeler.model.JWT;

import java.time.Instant;

import static io.realmarket.propeler.util.AuthUtils.TEST_AUTH;

public class JWTUtils {
  public static final String TEST_JWT_VALUE = "TEST_JWT_VALUE";

  public static final String TEST_HMAC_SECRET = "s3cr3t";

  public static final String TEST_JWT_REAL =
      "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfRU5UUkVQUkVORVVSIiwiaXNzIjoicmVhbG1hcmtldC5pbyIsImlhdCI6MTU1MzUxMTk3OSwianRpIjoiRjRWWFZNUldLWlhYWEFJUVpPVzRaVjMyNTVQSUdIS1QiLCJ1c2VybmFtZSI6InVzZXIifQ._74cxfPhYYX_5b2SRkZnY1yPfZ9lNPyhFn8B_1BAcHGyfJ_IozSuFqVuF4sTtnemaYANLRrn6TOj3HcOOfTkuA";

  public static final JWT TEST_JWT =
      JWT.builder().auth(TEST_AUTH).expirationTime(Instant.now()).value(TEST_JWT_VALUE).build();
}
