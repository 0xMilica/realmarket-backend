package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.CampaignInvestorDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "Campaign investors")
public interface CampaignInvestorController {

  @ApiOperation(
      value = "Create new investor",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Campaign successfully created."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 409, message = "Campaign with the provided name already exists.")
  })
  ResponseEntity<CampaignInvestorDto> createCampaignInvestor(
      String campaignName, CampaignInvestorDto campaignInvestorDto);

  @ApiOperation(
      value = "Change order of showing campaign investors",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Order of campaign investors changed."),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity patchCampaignInvestorOrder(
      String campaignName, List<Long> investorOrder);

  @ApiOperation(
      value = "Get all campaign investors",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Campaign successfully created."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<List<CampaignInvestorDto>> getCampaignInvestors(String campaignName);

  @ApiOperation(
      value = "Patch campaign investor",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Campaign successfully created."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 409, message = "Campaign with the provided name already exists.")
  })
  ResponseEntity patchCampaignInvestor(
      String campaignName, Long investorId, CampaignInvestorDto campaignInvestorDto);

  @ApiOperation(
      value = "Delete campaign investor",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 204, message = "Investor removed"),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity deleteCampaignInvestor(String campaignName, Long investorId);
}
