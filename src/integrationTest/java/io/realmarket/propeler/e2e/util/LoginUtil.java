package io.realmarket.propeler.e2e.util;

import io.realmarket.propeler.api.dto.LoginDto;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LoginUtil {

  public static String getJWTToken() {

    Response response =
        given()
            .cookie("remember-me", "1234")
            .contentType("application/json")
            .body(LoginDto.builder().username("entrepreneur").password("testPASS123").build())
            .when()
            .post("/api/auth");

    String token = response.jsonPath().getString("token");

    Map<String, Object> map = new HashMap<>();
    map.put("token", token);

    response =
        given()
            .cookie("remember-me", "1234")
            .contentType("application/json")
            .body(map)
            .when()
            .post("/api/auth/2fa/remember_me");

    return response.jsonPath().getString("jwt");
  }
}
