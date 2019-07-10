package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.FundraisingProposalResponseDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/fundraisingProposals")
public interface FundraisingProposalController {

  @ApiOperation(
      value = "Propose fundraising campaign idea.",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "fundraisingProposalDto",
        value = "FundraisingProposal's ID",
        required = true,
        dataType = "FundraisingProposalDto",
        paramType = "body")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Fundraising proposal successfully made."),
    @ApiResponse(code = 401, message = "")
  })
  ResponseEntity<FundraisingProposalResponseDto> makeFundraisingProposal(
      FundraisingProposalDto fundraisingProposalDto);

  @ApiOperation(
      value = "List pending fundraising proposals.",
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
          name = "filter",
          value = "State of fundraising proposal to be returned",
          allowableValues = "all, pending, approved, declined",
          defaultValue = "active",
          dataType = "String",
          paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Fundraising proposal successfully made."),
    @ApiResponse(code = 401, message = "LOG_005.")
  })
  ResponseEntity<Page<FundraisingProposalResponseDto>> getFundraisingProposals(Pageable pageable, String filter);
}
