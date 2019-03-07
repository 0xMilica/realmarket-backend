package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignTeamMemberDto;
import io.realmarket.propeler.api.dto.NewTeamMemberIdDto;
import io.realmarket.propeler.api.dto.TeamMemberPatchDto;

import java.util.List;

public interface CampaignTeamMemberService {

  NewTeamMemberIdDto createTeamMember(String campaignName, TeamMemberPatchDto teamMemberPatchDto);

  List<CampaignTeamMemberDto> getTeamForCampaign(String campaignName);

  List<CampaignTeamMemberDto> updateMembersOrder(String campaignName, List<Long> membersIds);

  CampaignTeamMemberDto updateTeamMember(
      String campaignName, Long teamMemberId, TeamMemberPatchDto teamMemberPatchDto);

  void deleteTeamMember(String campaignName, Long teamMemberId);
}
