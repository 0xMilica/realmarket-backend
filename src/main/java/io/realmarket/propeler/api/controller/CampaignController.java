package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.*;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.math.BigDecimal;
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
      dataType = "String",
      paramType = "path")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign successfully retrieved."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 404, message = "Campaign does not exists.")
  })
  ResponseEntity<CampaignResponseDto> getCampaign(String campaignName);

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
  ResponseEntity<CampaignResponseDto> patchCampaign(
      String campaignName, CampaignPatchDto campaignPatchDto);

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
  ResponseEntity deleteCampaignDocument(Long documentId);

  @ApiOperation(
      value = "Patch campaign document",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign document successfully modified."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<CampaignDocumentResponseDto> patchCampaignDocument(
      Long documentId, CampaignDocumentDto campaignDocumentDto);

  @ApiOperation(
      value = "Get all campaign documents grouped by document type.",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully retrieved all documents"),
    @ApiResponse(code = 401, message = "Unauthorized attempt to retrieve campaign documents.")
  })
  ResponseEntity<Map<String, List<CampaignDocumentResponseDto>>> getCampaignDocumentsGroupedByType(
      String campaignName);

  @ApiOperation(
      value = "Get campaign documents",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "campaignName",
      value = "Campaign's name",
      required = true,
      dataType = "String",
      paramType = "path")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully retrieved campaign documents."),
    @ApiResponse(code = 404, message = "Campaign not found.")
  })
  ResponseEntity<List<CampaignDocumentResponseDto>> getCampaignDocuments(String campaignName);

  @ApiOperation(
      value = "Send request for campaign documents access",
      httpMethod = "POST",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Campaign's url friendly name",
        required = true,
        dataType = "String",
        paramType = "path")
  })
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Successfully created request for campaign document access."),
    @ApiResponse(code = 404, message = "Campaign not found.")
  })
  ResponseEntity<CampaignDocumentsAccessRequestDto> sendCampaignDocumentsAccessRequest(
      String campaignName);

  @ApiOperation(
      value = "Get status of request for campaign documents access",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Campaign's url friendly name",
        required = true,
        dataType = "String",
        paramType = "path")
  })
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Successfully retrived status of request for campaign document access."),
    @ApiResponse(code = 404, message = "Campaign not found.")
  })
  ResponseEntity getCampaignDocumentsAccessRequestStatus(String campaignName);

  @ApiOperation(
      value = "Get requests for campaign documents access",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Successfully retrieved requests for campaign documents access.")
  })
  ResponseEntity getCampaignDocumentsAccessRequests();

  @ApiOperation(
      value = "Accept request for campaign documents access",
      httpMethod = "PATCH",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "requestId",
        value = "Campaign documents access request id",
        required = true,
        dataType = "Long",
        paramType = "path")
  })
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Successfully approved request for campaign document access."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 404, message = "Campaign documents access request not found.")
  })
  ResponseEntity<CampaignDocumentsAccessRequestDto> acceptCampaignDocumentsAccessRequest(
      Long requestId);

  @ApiOperation(
      value = "Reject request for campaign documents access",
      httpMethod = "PATCH",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "requestId",
        value = "Campaign documents access request id",
        required = true,
        dataType = "Long",
        paramType = "path")
  })
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Successfully rejected request for campaign document access."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 404, message = "Campaign documents access request not found.")
  })
  ResponseEntity<CampaignDocumentsAccessRequestDto> rejectCampaignDocumentsAccessRequest(
      Long requestId);

  @ApiOperation(
      value = "Get user's current campaign",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully retrieved current campaign"),
    @ApiResponse(code = 401, message = "Unauthorized attempt to retrieve campaign.")
  })
  ResponseEntity<CampaignResponseDto> getCurrentCampaign();

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

  @ApiOperation(
      value = "Get all campaign which publicly accessible",
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
        value = "State of campaign to be returned",
        allowableValues = "all, active, completed(successful, unsuccessful)",
        defaultValue = "active",
        dataType = "String",
        paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully retrieved all publicly accessible campaigns."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<Page<CampaignResponseDto>> getPublicCampaigns(Pageable pageable, String filter);

  @ApiOperation(
      value = "Get campaigns by state",
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
        allowableValues =
            "initial, review_ready, audit, active, launch_ready, completed(successful, unsuccessful), deleted",
        dataType = "String",
        paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully retrieved campaigns."),
    @ApiResponse(code = 404, message = "Campaign state not found.")
  })
  ResponseEntity<Page<CampaignResponseDto>> getCampaignsByState(Pageable pageable, String state);

  @ApiOperation(value = "List all accessible campaigns for user.", httpMethod = "GET")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaigns found."),
    @ApiResponse(code = 400, message = "Not authorized or insufficient data to process request."),
  })
  ResponseEntity getAllCampaignsForUser();

  @ApiOperation(
      value = "Convert amount of money to percent of equity",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        dataType = "String",
        value = "Campaign's name",
        paramType = "path",
        required = true),
    @ApiImplicitParam(
        name = "money",
        dataType = "Long",
        value = "Amount of money which need to be converted",
        paramType = "form",
        required = true)
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully convert money to percentage of equity."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 404, message = "Campaign not found.")
  })
  ResponseEntity<BigDecimal> convertMoneyToPercentageOfEquity(
      String campaignName, BigDecimal money);

  @ApiOperation(
      value = "Convert percent of equity to money",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        dataType = "String",
        value = "Campaign's name",
        paramType = "path",
        required = true),
    @ApiImplicitParam(
        name = "percentageOfEquity",
        dataType = "BigDecimal",
        value = "Percentage of equity which need to be converted",
        paramType = "form",
        required = true)
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully convert percentage of equity to money."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 404, message = "Campaign not found.")
  })
  ResponseEntity<BigDecimal> convertPercentageOfEquityToMoney(
      String campaignName, BigDecimal percentageOfEquity);

  @ApiOperation(value = "Get available equity for campaign.", httpMethod = "GET")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Name of the campaign that is being queried for available equity.",
        required = true,
        dataType = "String")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "There is available equity for campaign."),
    @ApiResponse(
        code = 400,
        message = "There is no campaign, no equity left, campaign is not active."),
  })
  ResponseEntity getAvailableEquity(String campaignName);

  @ApiOperation(value = "Get available investable amount for campaign.", httpMethod = "GET")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Name of the campaign that is being queried for available investable amount.",
        required = true,
        dataType = "String")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "There is available investable amount for campaign."),
    @ApiResponse(
        code = 400,
        message = "There is no campaign, no investable amount left, campaign is not active."),
  })
  ResponseEntity getAvailableInvestableAmount(String campaignName);

  @ApiOperation(
      value = "Get available equity and investable amount for campaign.",
      httpMethod = "GET")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value =
            "Name of the campaign that is being queried for available equity and investable amount.",
        required = true,
        dataType = "String")
  })
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "There is available equity and investable amount for campaign."),
    @ApiResponse(
        code = 400,
        message =
            "There is no campaign, no equity nor investable amount left, campaign is not active."),
  })
  ResponseEntity getAvailableInvestment(String campaignName);

  @ApiOperation(
      value = "Invest in campaign.",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "campaignName",
      value = "Campaign's name",
      required = true,
      dataType = "String")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Investment successfully submitted."),
    @ApiResponse(code = 404, message = "No campaign with given name."),
    @ApiResponse(code = 400, message = "Too large investment or campaign is inactive."),
  })
  ResponseEntity investInCampaign(AvailableInvestmentDto amountOfMoney, String campaignName);

  @ApiOperation(value = "Get user portfolio", httpMethod = "GET", produces = APPLICATION_JSON_VALUE)
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
        value = "State of campaign",
        allowableValues = "all, active, completed(successful, unsuccessful)",
        defaultValue = "all",
        dataType = "String",
        paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Portfolio successfully retrieved."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<Page<PortfolioCampaignResponseDto>> getPortfolio(Pageable pageable, String filter);

  @ApiOperation(
      value = "Create campaign update",
      httpMethod = "POST",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Campaign's name",
        required = true,
        dataType = "String",
        paramType = "path"),
    @ApiImplicitParam(
        name = "campaignUpdateDto",
        value = "Dto that contains information about campaign update",
        required = true,
        dataType = "CampaignUpdateDto",
        paramType = "body")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign update successfully created"),
    @ApiResponse(code = 404, message = "Campaign not found.")
  })
  ResponseEntity<CampaignUpdateResponseDto> createCampaignUpdate(
      String campaignName, CampaignUpdateDto campaignUpdateDto);

  @ApiOperation(
      value = "Update campaign update content",
      httpMethod = "PUT",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignUpdateId",
        value = "Campaign update identifier",
        required = true,
        dataType = "String",
        paramType = "path"),
    @ApiImplicitParam(
        name = "campaignUpdateDto",
        value = "Dto that contains information about campaign update",
        required = true,
        dataType = "CampaignUpdateDto",
        paramType = "body")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign update content successfully updated."),
    @ApiResponse(code = 404, message = "Campaign not found.")
  })
  ResponseEntity<CampaignUpdateResponseDto> updateCampaignUpdate(
      Long campaignUpdateId, CampaignUpdateDto campaignUpdateDto);

  @ApiOperation(
      value = "Upload campaign update image",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignUpdateId",
        value = "Campaign update identifier",
        required = true,
        dataType = "Long",
        paramType = "path"),
    @ApiImplicitParam(
        name = "image",
        value = "Image for campaign update content",
        required = true,
        dataType = "MultipartFile",
        paramType = "form")
  })
  @ApiResponses({
    @ApiResponse(code = 201, message = "Campaign update image successfully uploaded."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 404, message = "Campaign update not found.")
  })
  ResponseEntity<FilenameDto> uploadCampaignUpdateImage(Long campaignUpdateId, MultipartFile image);

  @ApiOperation(value = "Delete campaign update", httpMethod = "DELETE")
  @ApiImplicitParam(
      name = "campaignUpdateId",
      value = "Campaign update identifier",
      required = true,
      dataType = "Long",
      paramType = "path")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign update successfully deleted."),
    @ApiResponse(code = 404, message = "Campaign update not found")
  })
  ResponseEntity deleteCampaignUpdate(Long campaignUpdateId);

  @ApiOperation(
      value = "Get campaign update",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "campaignUpdateId",
      value = "Campaign update identifier",
      required = true,
      dataType = "Long",
      paramType = "path")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign update successfully retrieved."),
    @ApiResponse(code = 404, message = "Campaign does not exists.")
  })
  ResponseEntity<CampaignUpdateResponseDto> getCampaignUpdate(Long campaignUpdateId);

  @ApiOperation(
      value = "Get campaign updates for invested campaigns",
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
        value = "State of campaign",
        allowableValues = "all, my_campaigns, active, completed(successful, unsuccessful)",
        defaultValue = "all",
        dataType = "String",
        paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign updates successfully retrieved."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<Page<CampaignUpdateResponseDto>> getCampaignUpdates(
      Pageable pageable, String filter);

  @ApiOperation(
      value = "Get updates for campaign, pageable.",
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
        name = "campaignName",
        value = "Name of the campaign that is being queried for updates.",
        required = true,
        dataType = "String")
  })
  @ApiResponses({
    @ApiResponse(code = 204, message = "Campaign's updates were successfully fetched."),
    @ApiResponse(
        code = 403,
        message = "Campaign state is invalid, or there is no campaign under provided name."),
  })
  ResponseEntity listCampaignsUpdates(Pageable pageable, String campaignName);

  @ApiOperation(value = "Launch campaign", httpMethod = "PATCH", produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        dataType = "String",
        value = "Campaign's url friendly name",
        paramType = "path",
        required = true)
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign successfully launched."),
    @ApiResponse(code = 404, message = "Campaign not found")
  })
  ResponseEntity launchCampaign(String campaignName);

  @ApiOperation(value = "Close campaign", httpMethod = "PATCH", produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
          @ApiImplicitParam(
                  name = "campaignName",
                  dataType = "String",
                  value = "Campaign's url friendly name",
                  paramType = "path",
                  required = true),
          @ApiImplicitParam(
                  name = "CampaignClosingReasonDto",
                  value = "Dto that contains information about closing campaign.",
                  required = true,
                  dataType = "CampaignClosingReasonDto",
                  paramType = "body")
  })
  @ApiResponses({
          @ApiResponse(code = 200, message = "Campaign successfully closed"),
          @ApiResponse(code = 404, message = "Campaign not found")
  })
  ResponseEntity<CampaignResponseDto> closeCampaign(String campaignName, CampaignClosingReasonDto campaignClosingReasonDto);

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
        allowableValues = "initial, review_ready, audit, active, launch_ready, successful, unsuccessful",
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
