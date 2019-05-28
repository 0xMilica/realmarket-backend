package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.PlatformSettingsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/settings")
public interface PlatformSettingsController {

  @ApiOperation(
      value = "Gets current settings",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Settings successfully retrieved."),
    @ApiResponse(code = 404, message = "No current platform settings"),
  })
  ResponseEntity<PlatformSettingsDto> getCurrentPlatformSettings();

  @ApiOperation(value = "Get countries list", httpMethod = "GET", produces = APPLICATION_JSON_VALUE)
  @ApiResponses({@ApiResponse(code = 200, message = "Countires successfully retrieved.")})
  ResponseEntity getCountries();
}
