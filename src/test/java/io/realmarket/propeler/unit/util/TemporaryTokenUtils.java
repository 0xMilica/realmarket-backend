package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;

import java.time.Instant;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;

public class TemporaryTokenUtils {

  public static final Long TEST_EXPIRATION = 10000L;
  public static final String TEST_SECRET = "TEST_SECRET";
  public static final String TEST_TEMPORARY_TOKEN_VALUE = "TEST_TEMPORARY_TOKEN_VALUE";
  public static final ETemporaryTokenType TEST_TEMPORARY_TOKEN_TYPE =
      ETemporaryTokenType.REGISTRATION_TOKEN;
  public static final Instant TEST_EXPIRATION_TIME = Instant.now();

  public static final TemporaryToken TEST_TEMPORARY_TOKEN =
      TemporaryToken.builder()
          .auth(TEST_AUTH)
          .expirationTime(TEST_EXPIRATION_TIME)
          .temporaryTokenType(TEST_TEMPORARY_TOKEN_TYPE)
          .value(TEST_TEMPORARY_TOKEN_VALUE)
          .build();
}
