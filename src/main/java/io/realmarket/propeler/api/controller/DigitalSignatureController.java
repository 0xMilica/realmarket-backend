package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.DigitalSignaturePrivateDto;
import io.realmarket.propeler.api.dto.DigitalSignaturePublicDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/digitalSignatures")
public interface DigitalSignatureController {

  @ApiOperation(
      value = "Get my digital signature",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Digital signature successfully retrieved."),
    @ApiResponse(code = 404, message = "Digital signature does not exist.")
  })
  ResponseEntity<DigitalSignaturePrivateDto> getMyDigitalSignature();

  @ApiOperation(
      value = "Save digital signature",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "digitalSignaturePrivateDto",
        required = true,
        dataType = "DigitalSignaturePrivateDto",
        paramType = "body")
  })
  @ApiResponses({
    @ApiResponse(code = 201, message = "Digital signature successfully saved."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity saveDigitalSignature(DigitalSignaturePrivateDto digitalSignaturePrivateDto);

  @ApiOperation(
      value = "Get digital signature by auth",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(name = "authId", value = "Auth's id", required = true, dataType = "Long")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Digital signature successfully retrieved."),
    @ApiResponse(code = 404, message = "Digital signature does not exists.")
  })
  ResponseEntity<DigitalSignaturePublicDto> getDigitalSignature(Long authId);
}
