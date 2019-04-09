package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.CampaignTeamMemberDto;
import io.realmarket.propeler.api.dto.NewTeamMemberIdDto;
import io.realmarket.propeler.api.dto.TeamMemberPatchDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "Campaign team members")
public interface CampaignTeamMemberController {

  @ApiOperation(
      value = "Create new campaign team member",
      httpMethod = "POST",
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
    @ApiResponse(code = 201, message = "Campaign team member successfully created."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<NewTeamMemberIdDto> addTeamMember(
      String campaignName, TeamMemberPatchDto teamMemberPatchDto);

  @ApiOperation(
      value = "Delete existing campaign team member",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Name of the campaign that is being patched",
        required = true,
        dataType = "String"),
    @ApiImplicitParam(
        name = "teamMemberId",
        value = "Id of the campaign team member that is being deleted",
        required = true,
        dataType = "Long")
  })
  @ApiResponses({
    @ApiResponse(code = 204, message = "Campaign team member successfully deleted."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity deleteTeamMember(String campaignName, Long teamMemberId);

  @ApiOperation(
      value = "Get all team members for campaign",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Name of the campaign that is being patched",
        required = true,
        dataType = "String")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign team members successfully retrieved."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<List<CampaignTeamMemberDto>> getAllTeamMembers(String campaignName);

  @ApiOperation(
      value = "Update team member",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "campaignName",
        value = "Name of the campaign that is being patched",
        required = true,
        dataType = "String"),
    @ApiImplicitParam(
        name = "teamMemberId",
        value = "Id of the campaign team member that is being patched",
        required = true,
        dataType = "Long")
  })
  @ApiResponses({
    @ApiResponse(code = 204, message = "Campaign team member successfully updated."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<CampaignTeamMemberDto> updateTeamMember(
      String campaignName, Long teamMemberId, TeamMemberPatchDto campaignTeamMemberDto);

  @ApiOperation(
      value = "Reorder campaign team member",
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
    @ApiResponse(code = 201, message = "Campaign team members order successfully updated."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<List<CampaignTeamMemberDto>> updateAllTeamMembers(
      String campaignName, List<Long> membersIds);
}
