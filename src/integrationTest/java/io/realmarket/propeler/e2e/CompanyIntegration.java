package io.realmarket.propeler.e2e;

import io.realmarket.propeler.e2e.util.CompanyUtils;
import io.realmarket.propeler.e2e.util.IntegrationTestEnvironmentPreparator;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.realmarket.propeler.e2e.util.CompanyUtils.TEST_NAME;
import static io.realmarket.propeler.e2e.util.CompanyUtils.TEST_TAX_IDENTIFIER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompanyIntegration extends IntegrationTestEnvironmentPreparator {

  @Test
  public void b_registration() {
    given()
        .auth()
        .oauth2(getEntrepreneurJwt())
        .contentType("application/json")
        .accept("*/*, application/json")
        .when()
        .body(CompanyUtils.getCompanyDtoMocked())
        .post("/api/companies")
        .then()
        .statusCode(201)
        .assertThat()
        .body("id", equalTo(1))
        .body("taxIdentifier", equalTo(TEST_TAX_IDENTIFIER))
        .body("name", equalTo(TEST_NAME));
  }

  @Test
  public void c_registration_invalidRole_asInvestor() {
    given()
        .auth()
        .oauth2(getInvestorJwt())
        .contentType("application/json")
        .accept("*/*, application/json")
        .when()
        .body(CompanyUtils.getCompanyDtoMocked())
        .post("/api/companies")
        .then()
        .statusCode(400);
  }

  @Test
  public void c_registration_invalidRole_asAdmin() {
    given()
        .auth()
        .oauth2(getAdminJwt())
        .contentType("application/json")
        .accept("*/*, application/json")
        .when()
        .body(CompanyUtils.getCompanyDtoMocked())
        .post("/api/companies")
        .then()
        .statusCode(400);
  }

  @Test
  public void ba_registration_invalid_doubleRegistration() {
    given()
        .auth()
        .oauth2(getEntrepreneurJwt())
        .contentType("application/json")
        .accept("*/*, application/json")
        .when()
        .body(CompanyUtils.getCompanyDtoMocked())
        .post("/api/companies")
        .then()
        .statusCode(403);
  }

  @Test
  public void a_get_invalid_userHasNoCompany() {
    given()
        .auth()
        .oauth2(getEntrepreneurJwt())
        .accept("*/*, application/json")
        .when()
        .get("/api/companies/mine")
        .then()
        .statusCode(404);
  }

  @Test
  public void bb_get() {
    given()
        .auth()
        .oauth2(getEntrepreneurJwt())
        .accept("*/*, application/json")
        .when()
        .get("/api/companies/mine")
        .then()
        .statusCode(200)
        .assertThat()
        .body("id", equalTo(1))
        .body("taxIdentifier", equalTo(TEST_TAX_IDENTIFIER))
        .body("name", equalTo(TEST_NAME));
  }
}
