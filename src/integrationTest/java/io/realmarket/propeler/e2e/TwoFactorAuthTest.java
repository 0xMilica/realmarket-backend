package io.realmarket.propeler.e2e;

import io.realmarket.propeler.api.controller.impl.TwoFactorAuthControllerImpl;
import io.realmarket.propeler.api.dto.TwoFASecretRequestDto;
import io.realmarket.propeler.api.dto.TwoFASecretVerifyDto;
import io.realmarket.propeler.service.TwoFactorAuthService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.response.Response;
import org.jboss.aerogear.security.otp.Totp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration_testing")
public class TwoFactorAuthTest {

  @Mock private TwoFactorAuthService twoFactorAuthService;

  @InjectMocks private TwoFactorAuthControllerImpl twoFactorAuthController;

  @Before
  public void initialiseRestAssuredMockMvcWebApplicationContext() {
    RestAssuredMockMvc.standaloneSetup(twoFactorAuthController);
  }

  // RememberMe is already tested in LoginUtil

  @Test
  public void createAndVerifySecret() {
    TwoFASecretRequestDto twoFASecretRequestDto =
        TwoFASecretRequestDto.builder().setupToken("2faToken").build();

    Response response =
        given()
            .contentType("application/json")
            .accept("*/*, application/json")
            .body(twoFASecretRequestDto)
            .when()
            .post("/api/auth/2fa/secret")
            .then()
            .extract()
            .response();

    assertEquals(response.getStatusCode(), 200);

    String secret = response.path("secret");
    String token = response.path("token");

    Totp totp = new Totp(secret);
    String code = totp.now();

    TwoFASecretVerifyDto twoFASecretVerifyDto =
        TwoFASecretVerifyDto.builder().code(code).token(token).build();

    response =
        given()
            .contentType("application/json")
            .accept("*/*, application/json")
            .body(twoFASecretVerifyDto)
            .when()
            .post("/api/auth/2fa/verify")
            .then()
            .extract()
            .response();

    List<String> wildcards = response.path("wildcards");
    assertEquals(wildcards.size(), 10);
    assertEquals(wildcards.get(0).length(), 12);
  }
}
