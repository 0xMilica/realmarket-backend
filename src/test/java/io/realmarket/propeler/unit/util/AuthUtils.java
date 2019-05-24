package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthState;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.UserRole;
import io.realmarket.propeler.model.enums.EAuthState;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.security.UserAuthentication;
import io.realmarket.propeler.service.impl.EmailServiceImpl;
import io.realmarket.propeler.service.util.MailContentHolder;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import java.util.HashMap;

import static io.realmarket.propeler.service.util.RememberMeCookieHelper.COOKIE_NAME;
import static io.realmarket.propeler.unit.util.RememberMeCookieUtils.TEST_VALUE;

public class AuthUtils {

  public static final String TEST_USERNAME = "TEST_USERNAME";
  public static final String TEST_EMAIL = "TEST_EMAIL";
  public static final String TEST_PASSWORD = "TEST_PASSWORD";
  public static final String TEST_PASSWORD_NEW = "TEST_PASSWORD_NEW";
  public static final String TEST_ENCODED_SECRET = "enc_secret";
  public static final EUserRole TEST_ROLE = EUserRole.ROLE_INVESTOR;
  public static final EUserRole TEST_ROLE_ENTREPRENEUR = EUserRole.ROLE_ENTREPRENEUR;
  public static final EUserRole TEST_ROLE_FORBIDDEN = EUserRole.ROLE_ADMIN;
  public static final String TEST_TEMPORARY_TOKEN_VALUE = "TEST_TEMPORARY_TOKEN_VALUE";
  public static final Long TEST_AUTH_ID = 10L;
  public static final MailContentHolder TEST_EMAIL_DTO =
      new MailContentHolder(
          TEST_EMAIL,
          EEmailType.REGISTER,
          new HashMap<String, Object>() {
            {
              put(EmailServiceImpl.USERNAME, TEST_USERNAME);
              put(EmailServiceImpl.ACTIVATION_TOKEN, TEST_TEMPORARY_TOKEN_VALUE);
            }
          });
  public static final ChangePasswordDto TEST_CHANGE_PASSWORD_DTO =
      ChangePasswordDto.builder().oldPassword(TEST_PASSWORD).newPassword(TEST_PASSWORD_NEW).build();
  public static final PasswordDto TEST_PASSWORD_DTO = new PasswordDto(TEST_PASSWORD);
  public static final UsernameDto TEST_USERNAME_DTO = new UsernameDto(TEST_USERNAME);
  public static final ConfirmRegistrationDto TEST_CONFIRM_REGISTRATION_DTO =
      ConfirmRegistrationDto.builder().token(TEST_TEMPORARY_TOKEN_VALUE).build();
  public static final LoginDto TEST_LOGIN_DTO =
      LoginDto.builder().password(TEST_PASSWORD).username(TEST_USERNAME).build();
  public static final ResetPasswordDto TEST_RESET_PASSWORD_DTO =
      new ResetPasswordDto(TEST_TEMPORARY_TOKEN_VALUE, TEST_PASSWORD_NEW);
  private static final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
  private static final String TEST_LAST_NAME = "TEST_LAST_NAME";

  public static final UserRole TEST_USER_ROLE = UserRole.builder().name(TEST_ROLE).id(100L).build();
  public static final UserRole TEST_ENTREPRENEUR_USER_ROLE =
      UserRole.builder().name(TEST_ROLE_ENTREPRENEUR).id(101L).build();
  public static final AuthState TEST_AUTH_STATE =
      AuthState.builder().name(EAuthState.ACTIVE).id(100L).build();

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
  public static final Auth TEST_AUTH =
      Auth.builder()
          .id(TEST_AUTH_ID)
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(TEST_ENCODED_SECRET)
          .person(new Person(TEST_REGISTRATION_DTO))
          .blocked(false)
          .build();

  public static final Auth TEST_AUTH2 =
      Auth.builder()
          .id(TEST_AUTH_ID + 1)
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(TEST_ENCODED_SECRET)
          .person(new Person(TEST_REGISTRATION_DTO))
          .build();

  public static final Auth TEST_AUTH_ENTREPRENEUR =
      Auth.builder()
          .id(TEST_AUTH_ID + 1)
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_ENTREPRENEUR_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(TEST_ENCODED_SECRET)
          .person(new Person(TEST_REGISTRATION_DTO))
          .build();
  public static final Auth TEST_AUTH_OLD_SECRET =
      Auth.builder()
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(OTPUtils.TEST_SECRET_1)
          .person(new Person(TEST_REGISTRATION_DTO))
          .build();

  public static final ConfirmEmailChangeDto TEST_CONFIRM_EMAIL_CHANGE_DTO =
      ConfirmEmailChangeDto.builder().token(TEST_TEMPORARY_TOKEN_VALUE).build();
  public static final UserAuthentication TEST_USER_AUTH =
      new UserAuthentication(TEST_AUTH.toBuilder().build(), TEST_TEMPORARY_TOKEN_VALUE);
  public static final UserAuthentication TEST_USER_AUTH2 =
      new UserAuthentication(TEST_AUTH2.toBuilder().build(), TEST_TEMPORARY_TOKEN_VALUE);
  public static final UserAuthentication TEST_ENTREPRENEUR_USER_AUTH =
      new UserAuthentication(
          TEST_AUTH_ENTREPRENEUR.toBuilder().build(), TEST_TEMPORARY_TOKEN_VALUE);
  public static final MockHttpServletRequest TEST_REQUEST = new MockHttpServletRequest();
  public static final MockHttpServletResponse TEST_RESPONSE = new MockHttpServletResponse();
  public static final Cookie TEST_COOKIE = new Cookie(COOKIE_NAME, TEST_VALUE);

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

  static {
    TEST_REQUEST.setCookies(TEST_COOKIE);
  }

  public static void mockSecurityContext(UserAuthentication userAuthentication) {
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthentication);
    SecurityContextHolder.setContext(securityContext);
  }

  public static void mockRequestAndContext() {
    mockSecurityContext(TEST_USER_AUTH);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Forwarded-For", "localhost");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

  public static void mockRequestAndContextEntrepreneur() {
    mockSecurityContext(TEST_ENTREPRENEUR_USER_AUTH);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Forwarded-For", "localhost");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }
}
