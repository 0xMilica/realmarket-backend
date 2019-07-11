package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CampaignTeamMemberController;
import io.realmarket.propeler.api.dto.CampaignTeamMemberDto;
import io.realmarket.propeler.api.dto.NewTeamMemberIdDto;
import io.realmarket.propeler.api.dto.TeamMemberPatchDto;
import io.realmarket.propeler.service.CampaignTeamMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/campaigns/{campaignName}/team")
@Slf4j
public class CampaignTeamMemberControllerImpl implements CampaignTeamMemberController {
  private final CampaignTeamMemberService campaignTeamMemberService;

  @Autowired
  public CampaignTeamMemberControllerImpl(CampaignTeamMemberService campaignTeamMemberService) {
    this.campaignTeamMemberService = campaignTeamMemberService;
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<NewTeamMemberIdDto> addTeamMember(
      @PathVariable String campaignName,
      @RequestBody @Valid TeamMemberPatchDto teamMemberPatchDto) {
    return new ResponseEntity(
        campaignTeamMemberService.createTeamMember(campaignName, teamMemberPatchDto),
        HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/{teamMemberId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity deleteTeamMember(
      @PathVariable String campaignName, @PathVariable Long teamMemberId) {
    campaignTeamMemberService.deleteTeamMember(campaignName, teamMemberId);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<CampaignTeamMemberDto>> getAllTeamMembers(
      @PathVariable String campaignName) {
    return ResponseEntity.ok(campaignTeamMemberService.getTeamForCampaign(campaignName));
  }

  @PatchMapping(value = "/{teamMemberId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignTeamMemberDto> updateTeamMember(
      @PathVariable String campaignName,
      @PathVariable Long teamMemberId,
      @RequestBody @Valid TeamMemberPatchDto teamMemberPatchDto) {
    return ResponseEntity.ok(
        campaignTeamMemberService.updateTeamMember(campaignName, teamMemberId, teamMemberPatchDto));
  }

  @PatchMapping
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<List<CampaignTeamMemberDto>> updateAllTeamMembers(
      @PathVariable String campaignName, @RequestBody @Valid List<Long> membersIds) {
    return ResponseEntity.ok(
        campaignTeamMemberService.updateMembersOrder(campaignName, membersIds));
  }
}
