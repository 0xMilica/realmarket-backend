package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.EmailDto;
import io.realmarket.propeler.api.dto.RegistrationDto;
import io.realmarket.propeler.api.dto.ConfirmRegistrationDto;
import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.service.impl.EmailServiceImpl;

import java.util.Date;
import java.util.HashMap;

import static io.realmarket.propeler.unit.util.PersonUtils.getMockedPerson;

public class AuthUtils {
  public static final String TEST_USERNAME = "TEST_USERNAME";
  public static final String TEST_EMAIL = "TEST_EMAIL";
  public static final String TEST_PASSWORD = "TEST_PASSWORD";
  public static final EUserRole TEST_ROLE = EUserRole.ROLE_INVESTOR;
  public static final EUserRole TEST_ROLE_FORBIDDEN = EUserRole.ROLE_ADMIN;
  public static final String TEST_AUTH_TOKEN = "TEST_AUTH_TOKEN";

  public static final Date TEST_DATE = new Date();

  private static final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
  public static final EmailDto TEST_EMAIL_DTO =
      new EmailDto(
          TEST_EMAIL,
          EEmailType.REGISTER,
          new HashMap<String, Object>() {
            {
              put(EmailServiceImpl.FIRST_NAME, TEST_FIRST_NAME);
              put(EmailServiceImpl.LAST_NAME, TEST_FIRST_NAME);
              put(EmailServiceImpl.ACTIVATION_TOKEN, TEST_AUTH_TOKEN);
            }
          });
  private static final String TEST_LAST_NAME = "TEST_LAST_NAME";
  public static final RegistrationDto TEST_REGISTRATION_DTO =
      RegistrationDto.builder()
          .email(TEST_EMAIL)
          .username(TEST_USERNAME)
          .password(TEST_PASSWORD)
          .userRole(TEST_ROLE)
          .firstName(TEST_FIRST_NAME)
          .lastName(TEST_LAST_NAME)
          .countryOfResidence("TEST_COUNTRY_OF_RESIDENCE")
          .city("TEST_CITY")
          .address("TEST_ADDRESS")
          .build();

  public static final RegistrationDto TEST_REGISTRATION_DTO_ROLE_NOT_ALLOWED =
      RegistrationDto.builder()
          .email(TEST_EMAIL)
          .username(TEST_USERNAME)
          .password(TEST_PASSWORD)
          .userRole(TEST_ROLE_FORBIDDEN)
          .firstName(TEST_FIRST_NAME)
          .lastName(TEST_LAST_NAME)
          .countryOfResidence("TEST_COUNTRY_OF_RESIDENCE")
          .city("TEST_CITY")
          .address("TEST_ADDRESS")
          .build();

  public static final Auth TEST_AUTH =
      Auth.builder()
          .username(TEST_USERNAME)
          .active(false)
          .userRole(TEST_ROLE)
          .password(TEST_PASSWORD)
          .registrationToken(TEST_AUTH_TOKEN)
          .registrationTokenExpirationTime(TEST_DATE)
          .person(getMockedPerson(TEST_REGISTRATION_DTO))
          .build();

  public static final ConfirmRegistrationDto TEST_CONFIRM_REGISTRATION_DTO =
      ConfirmRegistrationDto.builder().token(TEST_AUTH_TOKEN).build();
}
