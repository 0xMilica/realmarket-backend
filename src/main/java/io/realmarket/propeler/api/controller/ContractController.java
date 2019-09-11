package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.ContractRequestDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/contracts")
public interface ContractController {
  @ApiOperation(
      value = "Get contract",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "contractRequestDto",
        value = "Dto that contains additional information needed for contract getting/generation.",
        required = true,
        dataType = "ContractRequestDto",
        paramType = "body"),
    @ApiImplicitParam(
        name = "contractType",
        value = "Contract Type",
        required = true,
        allowableValues = "CONTRACT",
        dataType = "String",
        paramType = "path")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Contract successfully found/created."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity getContract(String contractType, ContractRequestDto contractRequestDto);
}
