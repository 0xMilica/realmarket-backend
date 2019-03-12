package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.api.dto.CampaignDocumentResponseDto;
import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/campaigns")
public interface CampaignController {

  @ApiOperation(
      value = "Check if the campaign with the provided name already exists.",
      httpMethod = "HEAD")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Campaign name that is provided for existence checking")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign name already exists"),
    @ApiResponse(code = 404, message = "Campaign name is available"),
  })
  ResponseEntity campaignName(String campaignName);

  @ApiOperation(
      value = "Create new campaign",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Campaign successfully created."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity createCampaign(CampaignDto campaignDto);

  @ApiOperation(
      value = "Get campaigns's basic info",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "campaignName",
      value = "Campaign's name",
      required = true,
      dataType = "String")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign successfully retrieved."),
    @ApiResponse(code = 404, message = "Campaign does not exists.")
  })
  ResponseEntity<CampaignDto> getCampaign(String campaignName);

  @ApiOperation(
      value = "Patch campaign fields",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Name of the campaign that is being patched",
        required = true,
        dataType = "String")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Return campaign."),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity<CampaignDto> patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto);

  @ApiOperation(
      value = "Submit campaign document",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "campaignDocumentDto",
      value = "Dto that contains information about submitted document",
      required = true,
      dataType = "CampaignDocumentDto",
      paramType = "body")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully saved campaign document."),
    @ApiResponse(
        code = 400,
        message =
            "Campaign documents not saved. Check request body -  probably missing document type in request, or title and user id are blank.")
  })
  ResponseEntity<CampaignDocumentResponseDto> submitCampaignDocument(
      CampaignDocumentDto campaignDocumentDto, String campaignName);

  @ApiOperation(value = "Delete campaign document", httpMethod = "DELETE")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully deleted campaign document."),
    @ApiResponse(code = 404, message = "Campaign document does not exist.")
  })
  ResponseEntity deleteCampaignDocument(String campaignName, Long documentId);
}
