package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.*;
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
  @ApiImplicitParam(
          name = "userKYCRequestDto",
          value = "DTO with information about user KYC",
          required = true,
          dataType = "UserKYCRequestDto",
          paramType = "body")
  @ApiResponses({
    @ApiResponse(code = 200, message = "User KYC successfully requested."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<UserKYCDto> requestUserKYC(UserKYCRequestDto userKYCRequestDto);

  @ApiOperation(
      value = "Assign auditor to user KYC",
      httpMethod = "PATCH",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "userKYCAssignDto",
      value = "UserKYC's id and auditor id, TODO, FOR NOW, AUDITOR ID IS NOT USED!",
      required = true,
      dataType = "UserKYCAssignmentDto",
      paramType = "body")
  @ApiResponses({
    @ApiResponse(code = 200, message = "User KYC successfully assigned."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 404, message = "UKY_001")
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
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 404, message = "UKY_001")
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
        paramType = "query"),
    @ApiImplicitParam(
        name = "state",
        value = "State of user KYC",
        allowableValues = "all, pendingAssigned, pendingNotAssigned, approved, declined",
        defaultValue = "all",
        dataType = "String",
        paramType = "query"),
    @ApiImplicitParam(
        name = "role",
        value = "User role of user KYC",
        allowableValues = "all, investor, entrepreneur",
        defaultValue = "investor",
        dataType = "String",
        paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "User KYCs successfully found."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<Page<UserKYCResponseDto>> getUserKYCs(
      Pageable pageable, String state, String role);

  @ApiOperation(value = "Approve user KYC", httpMethod = "PATCH", produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "userKYCId",
      value = "UserKYC's id",
      required = true,
      dataType = "Long",
      paramType = "path")
  @ApiResponses({
    @ApiResponse(code = 200, message = "User KYC successfully approved."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 400, message = "UKY_002"),
    @ApiResponse(code = 400, message = "UKY_003"),
    @ApiResponse(code = 404, message = "UKY_001")
  })
  ResponseEntity<UserKYCDto> approveUserKYC(Long userKYCId);

  @ApiOperation(value = "Reject user KYC", httpMethod = "PATCH", produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "userKYCId",
        value = "UserKYC's id",
        required = true,
        dataType = "Long",
        paramType = "path"),
    @ApiImplicitParam(
        name = "userKYCRejectDto",
        value = "UserKYC's reason for rejection",
        required = true,
        dataType = "AuditDeclineDto",
        paramType = "body")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "User KYC successfully rejected."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 400, message = "UKY_002"),
    @ApiResponse(code = 400, message = "UKY_003"),
    @ApiResponse(code = 404, message = "UKY_001")
  })
  ResponseEntity<UserKYCDto> rejectUserKYC(Long UserKYCId, RejectionReasonDto userKYCRejectDto);
}
