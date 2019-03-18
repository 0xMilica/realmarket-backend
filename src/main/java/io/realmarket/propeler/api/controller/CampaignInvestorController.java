package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.CampaignInvestorDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

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
  ResponseEntity patchCampaignInvestorOrder(String campaignName, List<Long> investorOrder);

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

  @ApiOperation(
      value = "Upload investor picture",
      httpMethod = "POST",
      consumes = "multipart/form-data",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "Investor picture",
          dataType = "file",
          value = "Investor to be uploaded",
          paramType = "form",
          required = true))
  @ApiResponses({
    @ApiResponse(code = 201, message = "Picture successfully uploaded."),
    @ApiResponse(code = 400, message = "Picture cannot be saved.")
  })
  ResponseEntity uploadPicture(String campaignName, Long investorId, MultipartFile picture);

  @ApiOperation(
      value = "Download investor picture",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Picture retrieved successfully."),
    @ApiResponse(code = 400, message = "Picture cannot be found.")
  })
  ResponseEntity<FileDto> downloadPicture(String campaignName, Long investorId);

  @ApiOperation(
      value = "Delete investor picture",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Picture successfully deleted."),
    @ApiResponse(code = 500, message = "Internal server error.")
  })
  ResponseEntity deletePicture(String campaignName, Long investorId);
}
