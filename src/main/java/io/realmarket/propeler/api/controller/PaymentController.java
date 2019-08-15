package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/payments")
public interface PaymentController {

  @ApiOperation(value = "Get payments", httpMethod = "GET", produces = APPLICATION_JSON_VALUE)
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
        name = "filter",
        value = "State of payments to be returned",
        allowableValues = "owner_approved, paid, expired",
        dataType = "String",
        paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Payments successfully retrieved"),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity getPayments(Pageable pageable, String filter);

  @ApiOperation(
      value = "Confirm bank transfer payment",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "investmentId",
        value = "Investment ID",
        required = true,
        dataType = "Long",
        paramType = "path"),
    @ApiImplicitParam(
        name = "paymentConfirmationDto",
        value = "Dto that contains information about confirmed bank transfer",
        required = true,
        dataType = "PaymentConfirmationDto",
        paramType = "body")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully confirmed bank transfer."),
    @ApiResponse(code = 404, message = "Payment not found.")
  })
  ResponseEntity confirmBankTransferPayment(
      Long investmentId, PaymentConfirmationDto paymentConfirmationDto);
}
