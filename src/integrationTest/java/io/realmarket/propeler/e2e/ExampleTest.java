package io.realmarket.propeler.e2e;

import io.realmarket.propeler.e2e.util.LoginUtil;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration_testing")
public class ExampleTest {

  private static String auth;

  @Autowired private WebApplicationContext webApplicationContext;

  @Before
  public void initialiseRestAssuredMockMvcWebApplicationContext() {
    RestAssuredMockMvc.webAppContextSetup(this.webApplicationContext);
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
