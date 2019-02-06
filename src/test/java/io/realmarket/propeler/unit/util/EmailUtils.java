package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.EmailDto;
import io.realmarket.propeler.api.dto.enums.EEmailType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EmailUtils {

  private EmailUtils() {}

  public static final String TEST_USER_EMAIL = "TEST_USER_EMAIL";
  public static final String TEST_ACTIVATION_TOKEN = "TEST_ACTIVATION_TOKEN";
  public static final String TEST_RESET_TOKEN = "TEST_RESET_TOKEN";
  public static final String TEST_EMAIL_TEXT = "TEST_EMAIL_TEXT";
  public static final String TEST_ACTIVATION_SUBJECT = "Propeler - Welcome";
  public static final String TEST_RESET_SUBJECT = "Propeler - Reset Password";

  public static final Map<String, Object> TEST_EMAIL_DATA =
      Collections.singletonMap("activationToken", TEST_ACTIVATION_TOKEN);

  public static final EmailDto TEST_VALID_REGISTRATION_EMAIL_DTO =
      new EmailDto(TEST_USER_EMAIL, EEmailType.REGISTER, TEST_EMAIL_DATA);

  public static final EmailDto TEST_INVALID_REGISTRATION_EMAIL_DTO =
      new EmailDto(TEST_USER_EMAIL, EEmailType.REGISTER, new HashMap<>());

  public static final EmailDto TEST_VALID_RESET_EMAIL_DTO =
      new EmailDto(
          TEST_USER_EMAIL,
          EEmailType.RESET_PASSWORD,
          Collections.singletonMap("resetToken", TEST_RESET_TOKEN));

  public static final EmailDto TEST_INVALID_RESET_EMAIL_DTO =
      new EmailDto(TEST_USER_EMAIL, EEmailType.RESET_PASSWORD, new HashMap<>());
}
