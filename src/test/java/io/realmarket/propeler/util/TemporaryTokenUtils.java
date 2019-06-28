package io.realmarket.propeler.util;

import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.TemporaryTokenType;
import io.realmarket.propeler.model.enums.TemporaryTokenTypeName;

import java.time.Instant;

import static io.realmarket.propeler.util.AuthUtils.TEST_AUTH;

public class TemporaryTokenUtils {

  public static final Long TEST_EXPIRATION = 10000L;
  public static final String TEST_SECRET = "TEST_SECRET";
  public static final String TEST_TEMPORARY_TOKEN_VALUE = "TEST_TEMPORARY_TOKEN_VALUE";
  public static final TemporaryTokenTypeName TEST_TEMPORARY_TOKEN_ENUM_TYPE =
      TemporaryTokenTypeName.REGISTRATION_TOKEN;
  public static final TemporaryTokenType TEST_TEMPORARY_TOKEN_TYPE =
      TemporaryTokenType.builder().name(TemporaryTokenTypeName.REGISTRATION_TOKEN).id(100L).build();
  public static final Instant TEST_EXPIRATION_TIME = Instant.now();

  public static final TemporaryToken TEST_TEMPORARY_TOKEN =
      TemporaryToken.builder()
          .auth(TEST_AUTH)
          .expirationTime(TEST_EXPIRATION_TIME)
          .temporaryTokenType(
              TemporaryTokenType.builder().name(TEST_TEMPORARY_TOKEN_ENUM_TYPE).id(100L).build())
          .value(TEST_TEMPORARY_TOKEN_VALUE)
          .build();

  public static final TemporaryToken TEST_TEMPORARY_2FA_SETUP_TOKEN =
      TemporaryToken.builder()
          .auth(TEST_AUTH)
          .expirationTime(TEST_EXPIRATION_TIME)
          .temporaryTokenType(
              TemporaryTokenType.builder()
                  .name(TemporaryTokenTypeName.SETUP_2FA_TOKEN)
                  .id(101L)
                  .build())
          .value(TEST_TEMPORARY_TOKEN_VALUE)
          .build();
}
