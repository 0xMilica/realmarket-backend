package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.model.EmailChangeRequest;

import static io.realmarket.propeler.unit.util.TemporaryTokenUtils.TEST_TEMPORARY_TOKEN;

public class EmailChangeRequestUtils {

  public static final String TEST_NEW_EMAIL = "TEST_NEW_EMAIL";

  public static final EmailChangeRequest TEST_EMAIL_CHANGE_REQUEST =
      EmailChangeRequest.builder()
          .newEmail(TEST_NEW_EMAIL)
          .temporaryToken(TEST_TEMPORARY_TOKEN)
          .build();
}
