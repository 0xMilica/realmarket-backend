package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.*;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/fundraisingProposals")
public interface FundraisingProposalController {

  @ApiOperation(
      value = "Propose fundraising campaign idea",
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
      value = "Get fundraising proposal",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "fundraisingProposalId",
        value = "Fundraising proposal identifier",
        dataType = "Long",
        paramType = "path",
        required = true)
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Fundraising proposal successfully retrieved."),
    @ApiResponse(code = 404, message = "Fundraising proposal not found.")
  })
  ResponseEntity<FundraisingProposalResponseDto> getFundraisingProposal(Long fundraisingProposalId);

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
  ResponseEntity<Page<FundraisingProposalResponseDto>> getFundraisingProposals(
      Pageable pageable, String filter);

  @ApiOperation(
      value = "Submit fundraising proposal document",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "fundraisingProposalDocumentDto",
        value = "Dto that contains information about submitted document",
        required = true,
        dataType = "fundraisingProposalDocumentDto",
        paramType = "body"),
    @ApiImplicitParam(
        name = "fundraisingProposalId",
        value = "Fundrasing proposal identifier",
        dataType = "Long",
        paramType = "path",
        required = true)
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully saved fundraising proposal document."),
    @ApiResponse(
        code = 400,
        message =
            "Fundraising proposal document documents not saved. "
                + "Check request body - probably missing document type in request, or title and user id are blank."),
    @ApiResponse(code = 404, message = "Fundraising proposal not found.")
  })
  ResponseEntity<FundraisingProposalDocumentResponseDto> submitFundraisingProposalDocument(
      FundraisingProposalDocumentDto fundraisingProposalDocumentDto, Long fundraisingProposalId);

  @ApiOperation(
      value = "Get fundraising proposal documents",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "fundraisingProposalId",
        value = "Fundraising proposal identifier",
        dataType = "Long",
        paramType = "path",
        required = true)
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully retrieved fundraising proposal documents."),
    @ApiResponse(code = 404, message = "Fundraising proposal not found.")
  })
  ResponseEntity<List<FundraisingProposalDocumentResponseDto>> getFundraisingProposalDocuments(
      Long fundraisingProposalId);

  @ApiOperation(
      value = "Reject fundraising proposal",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "fundraisingProposalId",
        value = "Fundraising proposal identifier",
        dataType = "Long",
        paramType = "path",
        required = true),
    @ApiImplicitParam(
        name = "auditDeclineDto",
        value = "Fundraising proposal's rejection reason",
        required = true,
        dataType = "AuditDeclineDto",
        paramType = "body")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully rejected fundraising proposal."),
    @ApiResponse(code = 404, message = "Fundraising proposal not found.")
  })
  ResponseEntity<FundraisingProposalResponseDto> rejectFundraisingProposal(
      Long fundraisingProposalId, RejectionReasonDto rejectionReasonDto);

  @ApiOperation(
      value = "Accept fundraising proposal",
      httpMethod = "PATCH",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "fundraisingProposalId",
        value = "Fundraising proposal identifier",
        dataType = "Long",
        paramType = "path",
        required = true)
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully accepted fundraising proposal."),
    @ApiResponse(code = 404, message = "Fundraising proposal not found.")
  })
  ResponseEntity<FundraisingProposalResponseDto> acceptFundraisingProposal(
      Long fundraisingProposalId);
}
