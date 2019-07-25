package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.UserKYCAssignmentDto;
import io.realmarket.propeler.api.dto.UserKYCDto;
import io.realmarket.propeler.api.dto.UserKYCResponseDto;
import io.realmarket.propeler.api.dto.UserKYCResponseWithFilesDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api
public interface KYCController {

  @ApiOperation(
      value = "Request user KYC for entrepreneur or investor",
      httpMethod = "POST",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "User KYC successfully requested."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<UserKYCDto> requestUserKYC();

  @ApiOperation(
      value = "Assign auditor to user KYC",
      httpMethod = "PATCH",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "userKYCAssignDto",
      value = "UserKYC's id and auditor id",
      required = true,
      dataType = "UserKYCAssignmentDto",
      paramType = "body")
  @ApiResponses({
    @ApiResponse(code = 200, message = "User KYC successfully assigned."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<UserKYCDto> assignUserKYC(UserKYCAssignmentDto userKYCAssignmentDto);


  @ApiOperation(
      value = "Get user KYC request",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "userKYCId",
      value = "UserKYC's id",
      required = true,
      dataType = "Long",
      paramType = "path")
  @ApiResponses({
      @ApiResponse(code = 200, message = "User KYC successfully found."),
      @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<UserKYCResponseWithFilesDto> getUserKYC(Long userKYCId);

  @ApiOperation(
      value = "List user KYC requests",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
      @ApiImplicitParam(
          name = "page",
          value = "Number of page to be returned",
          defaultValue = "20",
          dataType = "Integer",
          paramType = "query"),
      @ApiImplicitParam(
          name = "size",
          value = "Page size (number of items to be returned)",
          defaultValue = "0",
          dataType = "Integer",
          paramType = "query")
  })
  @ApiResponses({
      @ApiResponse(code = 200, message = "User KYCs successfully found."),
      @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<Page<UserKYCResponseDto>> getUserKYCs(Pageable pageable);
}
