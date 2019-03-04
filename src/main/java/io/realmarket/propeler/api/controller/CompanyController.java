package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.CompanyDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/companies")
public interface CompanyController {

  @ApiOperation(
      value = "Create new company",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Company successfully created."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity createCompany(CompanyDto companyDto);

  @ApiOperation(
      value = "Get company's basic info",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(name = "id", value = "Company's id", required = true, dataType = "Long")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Company successfully retrieved."),
    @ApiResponse(code = 404, message = "Company does not exists.")
  })
  ResponseEntity getCompany(Long id);
}
