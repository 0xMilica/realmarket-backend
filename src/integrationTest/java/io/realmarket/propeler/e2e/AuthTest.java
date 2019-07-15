package io.realmarket.propeler.e2e;

import io.realmarket.propeler.api.controller.impl.AuthControllerImpl;
import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.e2e.util.LoginUtil;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.service.AuthService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration_testing")
public class AuthTest {

  @Mock private AuthService authService;

  @InjectMocks private AuthControllerImpl authController;

  @Before
  public void initialiseRestAssuredMockMvcWebApplicationContext() {
    RestAssuredMockMvc.standaloneSetup(authController);
  }

  @Test
  public void register() {
    RegistrationDto registrationDto =
        RegistrationDto.builder()
            .email("test@mail.com")
            .username("testRegistration")
            .password("testPassword")
            .userRole(UserRoleName.ROLE_ENTREPRENEUR)
            .firstName("testFirstName")
            .lastName("testLastName")
            .countryOfResidence("RS")
            .countryForTaxation("BB")
            .city("Novi Sad")
            .address("Modene 1")
            .build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .header("captcha_response", "captcha")
        .body(registrationDto)
        .when()
        .post("/api/auth/register")
        .then()
        .statusCode(201);
  }

  @Test
  public void register_failed_usernameExist() {
    RegistrationDto registrationDto =
        RegistrationDto.builder()
            .email("test@mail.com")
            .username("entrepreneur")
            .password("testPassword")
            .userRole(UserRoleName.ROLE_INVESTOR)
            .firstName("testFirstName")
            .lastName("testLastName")
            .countryOfResidence("RS")
            .city("Novi Sad")
            .address("Modene 1")
            .build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .header("captcha_response", "captcha")
        .body(registrationDto)
        .when()
        .post("/api/auth/register")
        .then()
        .statusCode(400)
        .body("message", equalTo("REG_001"));
  }

  @Test
  public void register_failed_roleNotAllowed() {
    RegistrationDto registrationDto =
        RegistrationDto.builder()
            .email("test@mail.com")
            .username("test123")
            .password("testPassword")
            .userRole(UserRoleName.ROLE_ADMIN)
            .firstName("testFirstName")
            .lastName("testLastName")
            .countryOfResidence("RS")
            .city("Novi Sad")
            .address("Modene 1")
            .build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .header("captcha_response", "captcha")
        .body(registrationDto)
        .when()
        .post("/api/auth/register")
        .then()
        .statusCode(400)
        .body("message", equalTo("REQ_001"));
  }

  @Test
  public void register_failed_invalidCountryCode() {
    RegistrationDto registrationDto =
        RegistrationDto.builder()
            .email("test@mail.com")
            .username("test123")
            .password("testPassword")
            .userRole(UserRoleName.ROLE_ENTREPRENEUR)
            .firstName("testFirstName")
            .lastName("testLastName")
            .countryOfResidence("RSA")
            .city("Novi Sad")
            .address("Modene 1")
            .build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .header("captcha_response", "captcha")
        .body(registrationDto)
        .when()
        .post("/api/auth/register")
        .then()
        .statusCode(400)
        .body("message", equalTo("CTR_001"));
  }

  @Test
  public void confirmRegistration() {
    ConfirmRegistrationDto confirmRegistrationDto =
        ConfirmRegistrationDto.builder().token("verificationToken").build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .body(confirmRegistrationDto)
        .when()
        .post("/api/auth/confirm_registration")
        .then()
        .statusCode(200);
  }

  @Test
  public void confirmRegistration_failed_badToken() {
    ConfirmRegistrationDto confirmRegistrationDto =
        ConfirmRegistrationDto.builder().token("badVerificationToken").build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .body(confirmRegistrationDto)
        .when()
        .post("/api/auth/confirm_registration")
        .then()
        .statusCode(400)
        .body("message", equalTo("LOG_005"));
  }

  @Test
  public void initializeResetPassword() {
    UsernameDto usernameDto = UsernameDto.builder().username("testResetPassword").build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .body(usernameDto)
        .when()
        .post("/api/auth/reset_password")
        .then()
        .statusCode(201);
  }

  @Test
  public void initializeResetPassword_failed_usernameNotExist() {
    UsernameDto usernameDto = UsernameDto.builder().username("test123").build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .body(usernameDto)
        .when()
        .post("/api/auth/reset_password")
        .then()
        .statusCode(404)
        .body("message", equalTo("LOG_002"));
  }

  @Test
  public void finalizeResetPassword() {
    ResetPasswordDto resetPasswordDto =
        ResetPasswordDto.builder()
            .resetPasswordToken("resetPasswordToken")
            .newPassword("newPassword")
            .build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .body(resetPasswordDto)
        .when()
        .patch("/api/auth/reset_password")
        .then()
        .statusCode(200);
  }

  @Test
  public void finalizeResetPassword_failed_invalidToken() {
    ResetPasswordDto resetPasswordDto =
        ResetPasswordDto.builder()
            .resetPasswordToken("badResetPasswordToken")
            .newPassword("newPassword")
            .build();

    given()
        .contentType("application/json")
        .accept("*/*, application/json")
        .body(resetPasswordDto)
        .when()
        .patch("/api/auth/reset_password")
        .then()
        .statusCode(400)
        .body("message", equalTo("LOG_005"));
  }

  @Test
  public void loginAndLogout() {
    String auth = LoginUtil.getJWTToken();

    given()
        .auth()
        .oauth2(auth)
        .contentType("application/json")
        .when()
        .delete("/api/auth")
        .then()
        .statusCode(204);
  }

  @Test
  public void login_failed_invalidCredentials() {
    given()
        .cookie("remember-me", "1234")
        .contentType("application/json")
        .body(LoginDto.builder().username("test123").password("testPASS123").build())
        .when()
        .post("/api/auth")
        .then()
        .statusCode(400)
        .body("message", equalTo("LOG_001"));
  }

  @Test
  public void recoverUsername() {
    EmailDto emailDto = EmailDto.builder().email("test.recover.username@mailinator.com").build();

    given()
        .contentType("application/json")
        .body(emailDto)
        .when()
        .post("/api/auth/recover_username")
        .then()
        .statusCode(201);
  }

  @Test
  public void recoverUsername_failed_EmailNotExist() {
    EmailDto emailDto = EmailDto.builder().email("test.recover.username123@mailinator.com").build();

    given()
        .contentType("application/json")
        .body(emailDto)
        .when()
        .post("/api/auth/recover_username")
        .then()
        .statusCode(404)
        .body("message", equalTo("LOG_003"));
  }

  @Test
  public void finalizeEmailChange() {
    ConfirmEmailChangeDto confirmEmailChangeDto =
        ConfirmEmailChangeDto.builder().token("confirmEmailChangeToken").build();

    given()
        .contentType("application/json")
        .body(confirmEmailChangeDto)
        .when()
        .patch("/api/auth/email_confirm")
        .then()
        .statusCode(200);
  }

  @Test
  public void finalizeEmailChange_failed_InvalidToken() {
    ConfirmEmailChangeDto confirmEmailChangeDto =
        ConfirmEmailChangeDto.builder().token("badConfirmEmailChangeToken").build();

    given()
        .contentType("application/json")
        .body(confirmEmailChangeDto)
        .when()
        .patch("/api/auth/email_confirm")
        .then()
        .statusCode(400)
        .body("message", equalTo("LOG_005"));
  }
}
