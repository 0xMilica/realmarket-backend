package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.InvestmentResponseDto;
import io.realmarket.propeler.api.dto.OffPlatformInvestmentRequestDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/investments")
public interface InvestmentController {
  @ApiOperation(value = "Revoke investment", httpMethod = "DELETE")
  @ApiImplicitParam(
      name = "investID",
      value = "Investment's id",
      required = true,
      dataType = "Long")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Investment successfully revoked."),
    @ApiResponse(code = 400, message = "Insufficient privileges."),
    @ApiResponse(code = 404, message = "Investment does not exists.")
  })
  ResponseEntity<Void> revokeInvestment(Long investmentId);

  @ApiOperation(value = "Approve investment as owner", httpMethod = "PATCH")
  @ApiImplicitParam(
      name = "investID",
      value = "Investment's id",
      required = true,
      dataType = "Long")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Investment successfully approved."),
    @ApiResponse(code = 400, message = "Insufficient privileges."),
    @ApiResponse(code = 404, message = "Investment does not exists.")
  })
  ResponseEntity<Void> ownerApproveInvestment(Long investmentId);

  @ApiOperation(value = "Approve investment as auditor", httpMethod = "PATCH")
  @ApiImplicitParam(
      name = "investID",
      value = "Investment's id",
      required = true,
      dataType = "Long")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Investment successfully approved."),
    @ApiResponse(code = 400, message = "Insufficient privileges."),
    @ApiResponse(code = 404, message = "Investment does not exists.")
  })
  ResponseEntity<Void> auditorApproveInvestment(Long investmentId);

  @ApiOperation(value = "Reject investment as owner", httpMethod = "PATCH")
  @ApiImplicitParam(
      name = "investID",
      value = "Investment's id",
      required = true,
      dataType = "Long")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Investment successfully rejected."),
    @ApiResponse(code = 400, message = "Insufficient privileges."),
    @ApiResponse(code = 404, message = "Investment does not exists.")
  })
  ResponseEntity<Void> ownerRejectInvestment(Long investmentId);

  @ApiOperation(value = "Reject investment as auditor", httpMethod = "PATCH")
  @ApiImplicitParam(
      name = "investID",
      value = "Investment's id",
      required = true,
      dataType = "Long")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Investment successfully rejected."),
    @ApiResponse(code = 400, message = "Insufficient privileges."),
    @ApiResponse(code = 404, message = "Investment does not exists.")
  })
  ResponseEntity<Void> auditorRejectInvestment(Long investmentId);

  @ApiOperation(
      value = "Create investment for investor that is not a platform user.",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "OffPlatformInvestmentRequestDto",
        value = "Off-Platform investment data",
        required = true,
        dataType = "OffPlatformInvestmentRequestDto",
        paramType = "body"),
    @ApiImplicitParam(
        name = "campaignName",
        value = "Campaign's name",
        required = true,
        dataType = "String",
        paramType = "path")
  })
  @ApiResponses({
    @ApiResponse(code = 204, message = "Investment successfully registered."),
    @ApiResponse(code = 400, message = "Insufficient privileges.")
  })
  ResponseEntity<InvestmentResponseDto> registerOffPlatformInvestment(
      OffPlatformInvestmentRequestDto offPlatformInvestmentRequestDto,
      String campaignName);
}
