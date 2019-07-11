package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignTeamMemberDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.NewTeamMemberIdDto;
import io.realmarket.propeler.api.dto.TeamMemberPatchDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignTeamMember;
import io.realmarket.propeler.repository.CampaignTeamMemberRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignTeamMemberService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.FileUtils;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_REQUEST;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.TEAM_MEMBER_NOT_FOUND;

@Slf4j
@Service
public class CampaignTeamMemberServiceImpl implements CampaignTeamMemberService {

  private final CampaignTeamMemberRepository campaignTeamMemberRepository;
  private final CampaignService campaignService;
  private final ModelMapperBlankString modelMapperBlankString;
  private final CloudObjectStorageService cloudObjectStorageService;

  @Value(value = "${cos.file_prefix.campaign_team_member_picture}")
  private String campaignTeamMemberPicturePrefix;

  @Autowired
  CampaignTeamMemberServiceImpl(
      CampaignTeamMemberRepository campaignTeamMemberRepository,
      ModelMapperBlankString modelMapperBlankString,
      CampaignService campaignService,
      CloudObjectStorageService cloudObjectStorageService) {
    this.campaignTeamMemberRepository = campaignTeamMemberRepository;
    this.modelMapperBlankString = modelMapperBlankString;
    this.campaignService = campaignService;
    this.cloudObjectStorageService = cloudObjectStorageService;
  }

  @Override
  public NewTeamMemberIdDto createTeamMember(
      String campaignName, TeamMemberPatchDto teamMemberPatchDto) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNotOwnerOrNotEditable(campaign);

    CampaignTeamMember teamMember = new CampaignTeamMember();
    modelMapperBlankString.map(teamMemberPatchDto, teamMember);
    teamMember.setCampaign(campaign);
    teamMember.setOrderNumber(
        campaignTeamMemberRepository.countByCampaignUrlFriendlyName(campaignName));
    log.info("Creating new team member for campaign {}!", campaignName);
    return new NewTeamMemberIdDto(campaignTeamMemberRepository.save(teamMember).getId());
  }

  @Override
  public List<CampaignTeamMemberDto> getTeamForCampaign(String campaignName) {
    final Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNoAccess(campaign);
    return modelMapperBlankString.map(
        campaignTeamMemberRepository.findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(
            campaignName),
        new TypeToken<List<CampaignTeamMemberDto>>() {}.getType());
  }

  @Transactional
  @Override
  public List<CampaignTeamMemberDto> updateMembersOrder(
      String campaignName, List<Long> membersIds) {

    checkCampaignMembersAccess(campaignName);

    verifyMemberIdsList(campaignName, membersIds);

    membersIds.forEach(
        memberId ->
            campaignTeamMemberRepository.reorderTeamMembers(
                memberId, membersIds.indexOf(memberId)));
    log.info("Saving new order of campaign team members!");
    return getTeamForCampaign(campaignName);
  }

  @Override
  public CampaignTeamMemberDto updateTeamMember(
      String campaignName, Long teamMemberId, TeamMemberPatchDto teamMemberPatchDto) {
    checkCampaignMembersAccess(campaignName);
    CampaignTeamMember teamMember = findByIdOrThrowException(teamMemberId);
    modelMapperBlankString.map(teamMemberPatchDto, teamMember);
    log.info("Updating campaign team member!");

    return new CampaignTeamMemberDto(campaignTeamMemberRepository.save(teamMember));
  }

  @Transactional
  @Override
  public void deleteTeamMember(String campaignName, Long teamMemberId) {
    checkCampaignMembersAccess(campaignName);
    CampaignTeamMember teamMember = findByIdOrThrowException(teamMemberId);
    log.info("Deleting campaign team member!");
    campaignTeamMemberRepository.delete(teamMember);
    log.info("Updating order of campaign team members!");
    campaignTeamMemberRepository
        .findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(teamMember.getCampaign().getName())
        .forEach(
            member -> {
              if (member.getOrderNumber() > teamMember.getOrderNumber()) {
                member.setOrderNumber(member.getOrderNumber() - 1);
                campaignTeamMemberRepository.save(member);
              }
            });
  }

  @Override
  public void uploadPicture(String campaignName, Long teamMemberId, MultipartFile picture) {
    CampaignTeamMember campaignTeamMember = findByIdOrThrowException(teamMemberId);
    checkCampaignMembersAccess(campaignName);
    String extension = FileUtils.getExtensionOrThrowException(picture);
    String url =
        String.join(
            "",
            campaignTeamMemberPicturePrefix,
            campaignTeamMember.getId().toString(),
            ".",
            extension);
    cloudObjectStorageService.uploadAndReplace(campaignTeamMember.getPhotoUrl(), url, picture);
    campaignTeamMember.setPhotoUrl(url);
    campaignTeamMemberRepository.save(campaignTeamMember);
  }

  @Override
  public FileDto downloadPicture(String campaignName, Long teamMemberId) {
    return cloudObjectStorageService.downloadFileDto(
        findByIdOrThrowException(teamMemberId).getPhotoUrl());
  }

  @Override
  public void deletePicture(String campaignName, Long teamMemberId) {
    log.info("Delete team member[{}] picture requested", teamMemberId);
    CampaignTeamMember campaignTeamMember = findByIdOrThrowException(teamMemberId);
    checkCampaignMembersAccess(campaignName);
    cloudObjectStorageService.delete(campaignTeamMember.getPhotoUrl());
    campaignTeamMember.setPhotoUrl(null);
    campaignTeamMemberRepository.save(campaignTeamMember);
  }

  private CampaignTeamMember findByIdOrThrowException(Long teamMemberId) {
    return campaignTeamMemberRepository
        .findById(teamMemberId)
        .orElseThrow(() -> new EntityNotFoundException(TEAM_MEMBER_NOT_FOUND));
  }

  private void checkCampaignMembersAccess(String campaignName) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNotOwnerOrNotEditable(campaign);
  }

  private void verifyMemberIdsList(String campaignName, List<Long> membersIds) {
    List<CampaignTeamMember> campaignMembers =
        campaignTeamMemberRepository.findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(
            campaignName);

    if (!(membersIds.size() == campaignMembers.size()
        && campaignMembers.stream()
            .map(CampaignTeamMember::getId)
            .collect(Collectors.toList())
            .containsAll(membersIds))) {
      throw new ForbiddenOperationException(INVALID_REQUEST);
    }
  }
}
