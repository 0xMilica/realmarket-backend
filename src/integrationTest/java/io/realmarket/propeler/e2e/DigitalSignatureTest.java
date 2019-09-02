package io.realmarket.propeler.e2e;

import io.realmarket.propeler.e2e.util.DigitalSignatureUtils;
import io.realmarket.propeler.e2e.util.IntegrationTestEnvironmentPreparator;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DigitalSignatureTest extends IntegrationTestEnvironmentPreparator {

  private static final String ENDPOINT = "/api/digitalSignatures/";
  private static final String MEDIA_TYPE =
      MediaType.toString(Arrays.asList(MediaType.ALL, MediaType.APPLICATION_JSON));

  @Test
  public void a_get_mine_notFound() {
    given()
        .auth()
        .oauth2(getEntrepreneurJwt())
        .accept(MEDIA_TYPE)
        .when()
        .get(ENDPOINT + "mine")
        .then()
        .statusCode(404);
  }

  @Test
  public void b_post_forbidden() {
    given()
        .accept(MEDIA_TYPE)
        .when()
        .accept(MEDIA_TYPE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(DigitalSignatureUtils.TEST_DIGITAL_SIGNATURE_PRIVATE_DTO)
        .post(ENDPOINT)
        .then()
        .statusCode(401);
  }

  @Test
  public void c_post_ok() {
    given()
        .auth()
        .oauth2(getEntrepreneurJwt())
        .accept(MEDIA_TYPE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .body(DigitalSignatureUtils.TEST_DIGITAL_SIGNATURE_PRIVATE_DTO)
        .post(ENDPOINT)
        .then()
        .statusCode(201);
  }

  @Test
  public void d_get_mine_ok() {
    given()
        .auth()
        .oauth2(getEntrepreneurJwt())
        .accept(MEDIA_TYPE)
        .when()
        .get(ENDPOINT + "mine")
        .then()
        .statusCode(200)
        .assertThat()
        .body("publicKey", equalTo(DigitalSignatureUtils.TEST_PUBLIC_KEY))
        .body("encryptedPrivateKey", equalTo(DigitalSignatureUtils.TEST_ENCRYPTED_PRIVATE_KEY))
        .body("initialVector", equalTo(DigitalSignatureUtils.TEST_INITIAL_VECTOR))
        .body("salt", equalTo(DigitalSignatureUtils.TEST_SALT))
        .body("passLength", equalTo(DigitalSignatureUtils.TEST_PASS_LENGTH));
  }

  @Test
  public void e_get_public_forbidden() {
    given()
        .accept(MEDIA_TYPE)
        .when()
        .accept(MEDIA_TYPE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .get(ENDPOINT + DigitalSignatureUtils.TEST_AUTH_ID)
        .then()
        .statusCode(401);
  }

  @Test
  public void f_get_public_notFound() {
    given()
        .auth()
        .oauth2(getInvestorJwt())
        .accept(MEDIA_TYPE)
        .when()
        .accept(MEDIA_TYPE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .get(ENDPOINT + DigitalSignatureUtils.TEST_AUTH_ID2)
        .then()
        .statusCode(404);
  }

  @Test
  public void g_get_public_ok() {
    given()
        .auth()
        .oauth2(getInvestorJwt())
        .accept(MEDIA_TYPE)
        .when()
        .accept(MEDIA_TYPE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .get(ENDPOINT + DigitalSignatureUtils.TEST_AUTH_ID)
        .then()
        .statusCode(200)
        .assertThat()
        .body("publicKey", equalTo(DigitalSignatureUtils.TEST_PUBLIC_KEY));
  }
}
