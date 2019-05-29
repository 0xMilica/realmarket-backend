package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.*;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Map;

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
      value = "Delete campaign",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "campaignName",
      value = "Campaign's name",
      required = true,
      dataType = "String")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign successfully deleted."),
    @ApiResponse(code = 404, message = "Campaign does not exists.")
  })
  ResponseEntity<Void> deleteCampaign(String campaignName, TwoFADto twoFADto)
      throws AuthenticationException;

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
      value = "Upload campaign featured image",
      httpMethod = "POST",
      consumes = "multipart/form-data",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "market image",
          dataType = "file",
          value = "Market image to be uploaded",
          paramType = "form",
          required = true))
  @ApiResponses({
    @ApiResponse(code = 201, message = "Market image successfully uploaded."),
    @ApiResponse(code = 400, message = "Market image cannot be saved.")
  })
  ResponseEntity uploadMarketImage(String campaignName, MultipartFile picture);

  @ApiOperation(
      value = "Download campaign market image",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Market image retrieved successfully."),
    @ApiResponse(code = 400, message = "Market image cannot be found.")
  })
  ResponseEntity<FileDto> downloadMarketImage(String campaignName);

  @ApiOperation(
      value = "Delete campaign market image",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Market image successfully deleted."),
    @ApiResponse(code = 500, message = "Internal server error.")
  })
  ResponseEntity deleteMarketImage(String campaignName);

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

  @ApiOperation(
      value = "Get all campaign documents groped by document type.",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully retrieved all documents"),
    @ApiResponse(code = 401, message = "Unauthorized attempt to retrieve campaign documents.")
  })
  ResponseEntity<Map<String, List<CampaignDocumentResponseDto>>> getCampaignDocuments(
      String campaignName);

  @ApiOperation(
      value = "Get active campaign",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully retrieved active campaign"),
    @ApiResponse(code = 401, message = "Unauthorized attempt to retrieve campaign.")
  })
  ResponseEntity<CampaignDto> getActiveCampaign();

  @ApiOperation(
      value = "Upload team member picture",
      httpMethod = "POST",
      consumes = "multipart/form-data",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "Team member picture",
          dataType = "file",
          value = "Team member photo to be uploaded",
          paramType = "form",
          required = true))
  @ApiResponses({
    @ApiResponse(code = 201, message = "Picture successfully uploaded."),
    @ApiResponse(code = 400, message = "Picture cannot be saved.")
  })
  ResponseEntity uploadPicture(String campaignName, Long teamMemberId, MultipartFile picture);

  @ApiOperation(
      value = "Download team member picture",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Picture retrieved successfully."),
    @ApiResponse(code = 400, message = "Picture cannot be found.")
  })
  ResponseEntity<FileDto> downloadPicture(String campaignName, Long teamMemberId);

  @ApiOperation(
      value = "Delete team member picture",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Picture successfully deleted."),
    @ApiResponse(code = 500, message = "Internal server error.")
  })
  ResponseEntity deletePicture(String campaignName, Long teamMemberId);

  @ApiOperation(value = "Mark campaign as ready for review.", httpMethod = "PATCH")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Name of the campaign that is being marked as ready for review.",
        required = true,
        dataType = "String")
  })
  @ApiResponses({
    @ApiResponse(code = 204, message = "Campaign was successfully submitted for review."),
    @ApiResponse(
        code = 403,
        message =
            "Campaign state transition is invalid, insufficient privileges or there is no campaign under provided name."),
  })
  ResponseEntity prepareCampaign(String campaignName);
}
