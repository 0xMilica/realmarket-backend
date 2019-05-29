package io.realmarket.propeler.e2e;

import io.realmarket.propeler.e2e.util.LoginUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.net.ssl.*"})
public class ExampleTest {

  private static String auth;

  @BeforeClass
  public static void before() {
    auth = LoginUtil.getJWTToken();
  }

  @Test
  public void example() {
    given()
        .auth()
        .oauth2(auth)
        .contentType("application/json")
        .accept("*/*, application/json")
        .when()
        .get("/api/users/1")
        .then()
        .statusCode(200)
        .assertThat()
        .body("id", equalTo(1))
        .body("city", equalTo("Novi Sad"));
  }
}
