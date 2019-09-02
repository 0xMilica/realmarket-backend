package io.realmarket.propeler.e2e;

import io.realmarket.propeler.e2e.util.IntegrationTestEnvironmentPreparator;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PlatformSettingsTest extends IntegrationTestEnvironmentPreparator {

  private static final String ENDPOINT = "/api/settings/";
  private static final String MEDIA_TYPE =
      MediaType.toString(Arrays.asList(MediaType.ALL, MediaType.APPLICATION_JSON));
  private static final String COUNTRY = "countries";

  @Test
  public void get_settings_ok() {
    given()
        .accept(MEDIA_TYPE)
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .assertThat()
        .body(COUNTRY, notNullValue())
        .body("platformMinimumInvestment", equalTo(500))
        .body("platformCurrency.code", equalTo("EUR"));
  }

  @Test
  public void get_countries_ok() {
    given()
        .accept(MEDIA_TYPE)
        .when()
        .get(ENDPOINT + COUNTRY)
        .then()
        .statusCode(200)
        .assertThat()
        .body(COUNTRY, notNullValue());
  }

  @Test
  public void get_minimumInvestment_ok() {
    given()
        .accept(MEDIA_TYPE)
        .when()
        .get(ENDPOINT + "minimumInvestment")
        .then()
        .statusCode(200)
        .assertThat()
        .body(equalTo("500"));
  }

  @Test
  public void get_currency_ok() {
    given()
        .accept(MEDIA_TYPE)
        .when()
        .get(ENDPOINT + "currency")
        .then()
        .statusCode(200)
        .assertThat()
        .body("code", equalTo("EUR"));
  }
}
