package io.realmarket.propeler.api.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

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
}
