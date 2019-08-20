package io.realmarket.propeler.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/settings")
public interface PlatformSettingsController {

  @ApiOperation(value = "Get countries list", httpMethod = "GET", produces = APPLICATION_JSON_VALUE)
  @ApiResponses({@ApiResponse(code = 200, message = "Countires successfully retrieved.")})
  ResponseEntity getCountries();

  @ApiOperation(
      value = "Get platform minimum investment",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Platform minimum investment successfully retrieved.")
  })
  ResponseEntity getMinimumInvestment();

  @ApiOperation(
      value = "Get platform currency",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({@ApiResponse(code = 200, message = "Currency successfully retrieved.")})
  ResponseEntity getCurrency();
}
