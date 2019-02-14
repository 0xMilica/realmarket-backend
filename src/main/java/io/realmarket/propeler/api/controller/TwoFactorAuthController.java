package io.realmarket.propeler.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/auth/2fa")
public interface TwoFactorAuthController {

  @ApiOperation(
          value = "",
          httpMethod = "POST",
          consumes = APPLICATION_JSON_VALUE,
          produces = APPLICATION_JSON_VALUE)
  ResponseEntity login();

  @ApiOperation(
          value = "",
          httpMethod = "POST",
          consumes = APPLICATION_JSON_VALUE,
          produces = APPLICATION_JSON_VALUE)
  ResponseEntity createSecret();

  @ApiOperation(
          value = "",
          httpMethod = "POST",
          consumes = APPLICATION_JSON_VALUE,
          produces = APPLICATION_JSON_VALUE)
  ResponseEntity createRecoveryCodes();
}
