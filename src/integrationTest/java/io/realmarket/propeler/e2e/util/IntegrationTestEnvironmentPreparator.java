package io.realmarket.propeler.e2e.util;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration_testing")
@PowerMockIgnore({"javax.net.ssl.*"})
public class IntegrationTestEnvironmentPreparator {

  // users
  public static final String ENTREPRENEUR_USERNAME = "entrepreneur";
  public static final String ENTREPRENEUR_2_USERNAME = "entrepreneur2";
  public static final String INVESTOR_USERNAME = "investor";
  public static final String ADMIN_USERNAME = "admin";

  public static final String USER_PASSWORD = "testPASS123";

  // user token's
  private String entrepreneurJwt;
  private String entrepreneur2Jwt;
  private String investorJwt;
  private String adminJwt;

  @Autowired private WebApplicationContext webApplicationContext;

  @Before
  public void initialiseRestAssuredMockMvcWebApplicationContext() {
    RestAssuredMockMvc.webAppContextSetup(this.webApplicationContext);
    entrepreneurJwt = LoginUtil.getJWTToken(ENTREPRENEUR_USERNAME, USER_PASSWORD);
    entrepreneur2Jwt = LoginUtil.getJWTToken(ENTREPRENEUR_2_USERNAME, USER_PASSWORD);
    investorJwt = LoginUtil.getJWTToken(INVESTOR_USERNAME, USER_PASSWORD);
    adminJwt = LoginUtil.getJWTToken(ADMIN_USERNAME, USER_PASSWORD);
  }

  public String getEntrepreneurJwt() {
    return entrepreneurJwt;
  }

  public String getEntrepreneur2Jwt() {
    return entrepreneur2Jwt;
  }

  public String getInvestorJwt() {
    return investorJwt;
  }

  public String getAdminJwt() {
    return adminJwt;
  }

  @Test
  public void test() {
    System.out.println("DELETE THIS");
  }
}
