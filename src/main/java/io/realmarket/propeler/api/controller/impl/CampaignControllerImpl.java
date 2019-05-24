package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CampaignController;
import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.CampaignDocumentService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignTeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/campaigns")
public class CampaignControllerImpl implements CampaignController {

  private final CampaignService campaignService;
  private final CampaignDocumentService campaignDocumentService;
  private final CampaignTeamMemberService campaignTeamMemberService;

  @Autowired
  public CampaignControllerImpl(
      CampaignService campaignService,
      CampaignDocumentService campaignDocumentService,
      CampaignTeamMemberService campaignTeamMemberService) {
    this.campaignService = campaignService;
    this.campaignDocumentService = campaignDocumentService;
    this.campaignTeamMemberService = campaignTeamMemberService;
  }

  @RequestMapping(value = "/{campaignName}", method = RequestMethod.HEAD)
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity campaignName(@PathVariable String campaignName) {
    campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    return ResponseEntity.ok().build();
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity createCampaign(@RequestBody @Valid CampaignDto campaignDto) {
    campaignService.createCampaign(campaignDto);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @GetMapping(value = "/{campaignName}")
  public ResponseEntity<CampaignDto> getCampaign(@PathVariable String campaignName) {
    return ResponseEntity.ok(campaignService.getCampaignDtoByUrlFriendlyName(campaignName));
  }

  @DeleteMapping(value = "/{campaignName}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<Void> deleteCampaign(@PathVariable String campaignName) {
    campaignService.delete(campaignName);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping(value = "/{campaignName}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignDto> patchCampaign(
      @PathVariable String campaignName, @RequestBody @Valid CampaignPatchDto campaignPatchDto) {
    return ResponseEntity.ok(campaignService.patchCampaign(campaignName, campaignPatchDto));
  }

  @PostMapping("/{campaignName}/market_image")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity uploadMarketImage(
      @PathVariable String campaignName, @RequestParam("picture") MultipartFile picture) {
    campaignService.uploadMarketImage(campaignName, picture);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @GetMapping("/{campaignName}/market_image")
  public ResponseEntity<FileDto> downloadMarketImage(@PathVariable String campaignName) {
    return ResponseEntity.ok(campaignService.downloadMarketImage(campaignName));
  }

  @DeleteMapping("/{campaignName}/market_image")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity deleteMarketImage(@PathVariable String campaignName) {
    campaignService.deleteMarketImage(campaignName);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(value = "/{campaignName}/documents")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignDocumentResponseDto> submitCampaignDocument(
      @RequestBody @Valid CampaignDocumentDto campaignDocumentDto,
      @PathVariable String campaignName) {
    return ResponseEntity.ok(
        new CampaignDocumentResponseDto(
            campaignDocumentService.submitDocument(campaignDocumentDto, campaignName)));
  }

  @DeleteMapping(value = "/{campaignName}/documents/{documentId}")
  public ResponseEntity<Void> deleteCampaignDocument(
      @PathVariable String campaignName, @PathVariable Long documentId) {
    campaignDocumentService.deleteDocument(campaignName, documentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/{campaignName}/documents")
  public ResponseEntity<Map<String, List<CampaignDocumentResponseDto>>> getCampaignDocuments(
      @PathVariable String campaignName) {
    return ResponseEntity.ok(
        campaignDocumentService.getAllCampaignDocumentDtoGropedByType(campaignName));
  }

  @GetMapping(value = "/active")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignDto> getActiveCampaign() {
    return ResponseEntity.ok(campaignService.getActiveCampaignDto());
  }

  @PostMapping("/{campaignName}/team/{teamMemberId}/picture")
  public ResponseEntity uploadPicture(
      @PathVariable String campaignName,
      @PathVariable Long teamMemberId,
      @RequestParam("picture") MultipartFile picture) {
    campaignTeamMemberService.uploadPicture(campaignName, teamMemberId, picture);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/{campaignName}/team/{teamMemberId}/picture")
  public ResponseEntity<FileDto> downloadPicture(
      @PathVariable String campaignName, @PathVariable Long teamMemberId) {
    return ResponseEntity.ok(campaignTeamMemberService.downloadPicture(campaignName, teamMemberId));
  }

  @DeleteMapping("/{campaignName}/team/{teamMemberId}/picture")
  public ResponseEntity deletePicture(
      @PathVariable String campaignName, @PathVariable Long teamMemberId) {
    campaignTeamMemberService.deletePicture(campaignName, teamMemberId);
    return ResponseEntity.noContent().build();
  }
}
