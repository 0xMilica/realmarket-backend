package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.UserKYCDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api
public interface KYCController {

  @ApiOperation(
      value = "Request user KYC for entrepreneur or investor",
      httpMethod = "PATCH",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "User KYC successfully requested."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<UserKYCDto> requestUserKYC();
}
