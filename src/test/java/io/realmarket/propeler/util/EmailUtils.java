package io.realmarket.propeler.util;

import io.realmarket.propeler.service.util.email.message.custom.user.RegisterMessage;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmailUtils {

  public static final String TEST_USER_EMAIL = "TEST_USER_EMAIL";
  public static final String TEST_ACTIVATION_TOKEN = "TEST_ACTIVATION_TOKEN";
  public static final String TEST_RESET_TOKEN = "TEST_RESET_TOKEN";
  public static final String TEST_EMAIL_TEXT = "TEST_EMAIL_TEXT";
  public static final String TEST_ACTIVATION_SUBJECT = "Propeler - Welcome";
  public static final String TEST_RESET_SUBJECT = "Propeler - Reset Password";
  public static final String TEST_TEMPLATE_NAME = "TEST_TEMPLATE_NAME";
  public static final String TEST_FRONTEND_URL = "TEST_FRONTEND_URL";
  public static final Map<String, Object> TEST_EMAIL_DATA =
      Stream.of(new AbstractMap.SimpleEntry<>("activationToken", TEST_ACTIVATION_TOKEN))
          .collect(
              Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

  public static final RegisterMessage REGISTER_MESSAGE =
      new RegisterMessage(
          Arrays.asList(TEST_USER_EMAIL),
          TEST_EMAIL_DATA,
          TEST_ACTIVATION_SUBJECT,
          TEST_TEMPLATE_NAME,
          TEST_FRONTEND_URL);
  //  public static final EmailContentHolder TEST_VALID_REGISTRATION_EMAIL_DTO =
  //      new EmailContentHolder(Arrays.asList(TEST_USER_EMAIL), EmailType.REGISTER,
  // TEST_EMAIL_DATA);
  //  public static final EmailContentHolder TEST_INVALID_REGISTRATION_EMAIL_DTO =
  //      new EmailContentHolder(Arrays.asList(TEST_USER_EMAIL), EmailType.REGISTER, new
  // HashMap<>());
  //  public static final EmailContentHolder TEST_VALID_RESET_EMAIL_DTO =
  //      new EmailContentHolder(
  //          Arrays.asList(TEST_USER_EMAIL),
  //          EmailType.RESET_PASSWORD,
  //          Collections.singletonMap("resetToken", TEST_RESET_TOKEN));
  //  public static final EmailContentHolder TEST_INVALID_RESET_EMAIL_DTO =
  //      new EmailContentHolder(
  //          Arrays.asList(TEST_USER_EMAIL), EmailType.RESET_PASSWORD, new HashMap<>());
  //
  //
  //            Arrays.asList(TEST_EMAIL),
  //  EmailType.REGISTER,
  //          new HashMap<String, Object>() {
  //    {
  //      put(EmailServiceImpl.USERNAME, TEST_USERNAME);
  //      put(EmailServiceImpl.ACTIVATION_TOKEN, TEST_TEMPORARY_TOKEN_VALUE);
  //    }

  private EmailUtils() {}
}
