package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.OTPWildcard;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static io.realmarket.propeler.model.enums.EAuthorizationActionType.*;
import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.unit.util.AuthUtils.TEST_EMAIL;

public class OTPUtils {

  public static final String TEST_SECRET_1 = "235562";
  public static final String TEST_SECRET_2 = "696969";


  public static final String TEST_TOTP_CODE_1 = "123456";
  public static final String TEST_TOTP_CODE_2 = "678901";

  public static final String TEST_OTP_WILDCARD_1 = "OTP_WILDCARD_1";
  public static final String TEST_OTP_WILDCARD_2 = "OTP_WILDCARD_2";
  public static final String TEST_OTP_WILDCARD_3 = "OTP_WILDCARD_3";

  public static OTPWildcard TEST_OTP_WILDCARD_1() {
    return OTPWildcard.builder()
        .wildcard(TEST_OTP_WILDCARD_1)
        .auth(TEST_AUTH)
        .build();
  }

  public static OTPWildcard TEST_OTP_WILDCARD_2() {
    return OTPWildcard.builder()
        .wildcard(TEST_OTP_WILDCARD_2)
        .auth(TEST_AUTH)
        .build();
  }

  public static AuthorizedAction TEST_AUTH_ACTION_NEW2FA() {
    return AuthorizedAction.builder()
            .auth(TEST_AUTH)
            .data(TEST_SECRET_1)
            .expiration(Instant.now().plusMillis(1000000L))
            .type(NEW_TOTP_SECRET)
            .build();
  }

  public static AuthorizedAction TEST_AUTH_ACTION_NEWEMAIL() {
    return AuthorizedAction.builder()
            .auth(TEST_AUTH)
            .data(TEST_EMAIL)
            .expiration(Instant.now().plusMillis(1000000L))
            .type(NEW_EMAIL)
            .build();
  }

  public static AuthorizedAction TEST_AUTH_ACTION_NEW2FA_EXPIRED() {
    return AuthorizedAction.builder()
            .auth(TEST_AUTH)
            .data(TEST_SECRET_1)
            .expiration(Instant.now().minusMillis(1000000L))
            .type(NEW_TOTP_SECRET)
            .build();
  }

  public static List<String> TEST_OTP_WILDCARD_STRING_LIST() {
    return Arrays.asList(TEST_OTP_WILDCARD_1,TEST_OTP_WILDCARD_2,TEST_OTP_WILDCARD_3);
  }


  public static List<OTPWildcard> TEST_OTP_WILDCARD_LIST() {
    return Arrays.asList(TEST_OTP_WILDCARD_1(), TEST_OTP_WILDCARD_2());
  }
}
