package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.CampaignWithInvestmentsWithPersonResponseDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/campaignsWithInvestments")
public interface CampaignWithInvestmentController {
  @ApiOperation(
      value = "Get campaigns by state along with investments",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
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
        value = "State of campaign to be returned",
        allowableValues = "all, initial, review_ready, audit, active, launch_ready, post_campaign",
        defaultValue = "active",
        required = true,
        dataType = "String",
        paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully retrieved campaigns."),
    @ApiResponse(code = 404, message = "Campaign state not found.")
  })
  ResponseEntity<Page<CampaignWithInvestmentsWithPersonResponseDto>>
      getCampaignsByStateWithInvestments(Pageable pageable, String state);
}
