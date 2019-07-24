package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.AuditAssignmentDto;
import io.realmarket.propeler.api.dto.AuditDeclineDto;
import io.realmarket.propeler.api.dto.AuditResponseDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/audits")
public interface AuditController {

  @ApiOperation(
      value = "Audit campaign",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "auditAssignmentDto",
        value = "Dto that contains information about audit request",
        dataType = "AuditAssignmentDto",
        paramType = "body",
        required = true),
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign successfully audit."),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity<AuditResponseDto> assignAudit(AuditAssignmentDto auditAssignmentDto);

  @ApiOperation(
      value = "Accept a pending campaign's audit.",
      httpMethod = "PATCH",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "auditId",
        value = "Audits's ID",
        required = true,
        dataType = "Long",
        paramType = "path")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign accepted."),
    @ApiResponse(code = 403, message = "Access is denied."),
    @ApiResponse(code = 404, message = "Audit not found."),
    @ApiResponse(code = 400, message = "Audit state cannot be changed.")
  })
  ResponseEntity<AuditResponseDto> acceptAudit(Long auditId);

  @ApiOperation(
      value = "Decline a pending campaign's audit.",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "auditId",
        value = "Audits's ID",
        required = true,
        dataType = "Long",
        paramType = "path"),
    @ApiImplicitParam(
        name = "auditDeclineDto",
        value = "Campaign's rejection reason",
        required = true,
        dataType = "AuditDeclineDto",
        paramType = "body")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign rejected."),
    @ApiResponse(code = 403, message = "Access is denied."),
    @ApiResponse(code = 404, message = "Audit not found."),
    @ApiResponse(code = 400, message = "Audit state cannot be changed.")
  })
  ResponseEntity<AuditResponseDto> declineAudit(Long auditId, AuditDeclineDto auditDeclineDto);
}
