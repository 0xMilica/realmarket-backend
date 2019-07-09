package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CampaignController;
import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/campaigns")
public class CampaignControllerImpl implements CampaignController {

  private final CampaignService campaignService;
  private final CampaignDocumentService campaignDocumentService;
  private final CampaignTeamMemberService campaignTeamMemberService;
  private final CampaignUpdateService campaignUpdateService;
  private final CampaignUpdateImageService campaignUpdateImageService;
  private final InvestmentService investmentService;

  @Autowired
  public CampaignControllerImpl(
      CampaignService campaignService,
      CampaignDocumentService campaignDocumentService,
      CampaignTeamMemberService campaignTeamMemberService,
      CampaignUpdateService campaignUpdateService,
      CampaignUpdateImageService campaignUpdateImageService,
      InvestmentService investmentService) {
    this.campaignService = campaignService;
    this.campaignDocumentService = campaignDocumentService;
    this.campaignTeamMemberService = campaignTeamMemberService;
    this.campaignUpdateService = campaignUpdateService;
    this.campaignUpdateImageService = campaignUpdateImageService;
    this.investmentService = investmentService;
  }

  @RequestMapping(value = "/{campaignName}", method = RequestMethod.HEAD)
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity campaignName(@PathVariable String campaignName) {
    campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    return ResponseEntity.ok().build();
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignResponseDto> createCampaign(
      @RequestBody @Valid CampaignDto campaignDto) {
    return new ResponseEntity<>(campaignService.createCampaign(campaignDto), HttpStatus.CREATED);
  }

  @GetMapping(value = "/{campaignName}")
  public ResponseEntity<CampaignResponseDto> getCampaign(@PathVariable String campaignName) {
    return ResponseEntity.ok(campaignService.getCampaignDtoByUrlFriendlyName(campaignName));
  }

  @DeleteMapping(value = "/{campaignName}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<Void> deleteCampaign(
      @PathVariable String campaignName, @RequestBody @Valid TwoFADto twoFADto)
      throws AuthenticationException {
    campaignService.delete(campaignName, twoFADto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping(value = "/{campaignName}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignResponseDto> patchCampaign(
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

  @DeleteMapping(value = "/mine/documents/{documentId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<Void> deleteCampaignDocument(@PathVariable Long documentId) {
    campaignDocumentService.deleteDocument(documentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping(value = "/mine/documents/{documentId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignDocumentResponseDto> patchCampaignDocument(
      @PathVariable Long documentId, @RequestBody @Valid CampaignDocumentDto campaignDocumentDto) {
    return ResponseEntity.ok(
        new CampaignDocumentResponseDto(
            campaignDocumentService.patchCampaignDocument(documentId, campaignDocumentDto)));
  }

  @GetMapping(value = "/{campaignName}/documentsByType")
  public ResponseEntity<Map<String, List<CampaignDocumentResponseDto>>>
      getCampaignDocumentsGroupedByType(@PathVariable String campaignName) {
    return ResponseEntity.ok(
        campaignDocumentService.getAllCampaignDocumentDtoGroupedByType(campaignName));
  }

  @GetMapping(value = "/{campaignName}/documents")
  public ResponseEntity<List<CampaignDocumentResponseDto>> getCampaignDocuments(
      @PathVariable String campaignName) {
    return ResponseEntity.ok(campaignDocumentService.getCampaignDocuments(campaignName));
  }

  @GetMapping(value = "/mine/active")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignResponseDto> getActiveCampaign() {
    return ResponseEntity.ok(campaignService.getActiveCampaignDto());
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @GetMapping(value = "/auditCampaign/{campaignName}")
  public ResponseEntity<AuditCampaignResponseDto> getAuditCampaign(
      @PathVariable String campaignName) {
    return ResponseEntity.ok(campaignService.getAuditCampaign(campaignName));
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

  @PatchMapping(value = "/{campaignName}/review")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity prepareCampaign(@PathVariable String campaignName) {
    campaignService.requestReviewForCampaign(campaignName);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/public")
  public ResponseEntity<Page<CampaignResponseDto>> getPublicCampaigns(
      Pageable pageable,
      @RequestParam(value = "filter", required = false, defaultValue = "active") String filter) {
    return ResponseEntity.ok(campaignService.getPublicCampaigns(pageable, filter));
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Page<CampaignResponseDto>> getCampaignsByState(
      Pageable pageable, @RequestParam(value = "state", defaultValue = "active") String state) {
    return ResponseEntity.ok(campaignService.getCampaignsByState(pageable, state));
  }

  @GetMapping(value = "/mine")
  public ResponseEntity getAllCampaignsForUser() {
    return new ResponseEntity<>(campaignService.getAllCampaignsForUser(), HttpStatus.OK);
  }

  @GetMapping(value = "/{campaignName}/convertMoney")
  public ResponseEntity<BigDecimal> convertMoneyToPercentageOfEquity(
      @PathVariable String campaignName, BigDecimal money) {
    return ResponseEntity.ok(campaignService.convertMoneyToPercentageOfEquity(campaignName, money));
  }

  @GetMapping(value = "/{campaignName}/convertPercentage")
  public ResponseEntity<BigDecimal> convertPercentageOfEquityToMoney(
      @PathVariable String campaignName, BigDecimal percentageOfEquity) {
    return ResponseEntity.ok(
        campaignService.convertPercentageOfEquityToMoney(campaignName, percentageOfEquity));
  }

  @GetMapping(value = "/{campaignName}/availableEquity")
  @PreAuthorize("hasAnyAuthority('ROLE_INVESTOR', 'ROLE_ENTREPRENEUR')")
  public ResponseEntity getAvailableEquity(@PathVariable String campaignName) {
    return ResponseEntity.ok(campaignService.getAvailableEquity(campaignName));
  }

  @GetMapping(value = "/{campaignName}/availableInvestment")
  @PreAuthorize("hasAnyAuthority('ROLE_INVESTOR', 'ROLE_ENTREPRENEUR')")
  public ResponseEntity getAvailableInvestment(@PathVariable String campaignName) {
    return ResponseEntity.ok(campaignService.getAvailableInvestment(campaignName));
  }

  @GetMapping(value = "/{campaignName}/availableInvestableAmount")
  @PreAuthorize("hasAnyAuthority('ROLE_INVESTOR', 'ROLE_ENTREPRENEUR')")
  public ResponseEntity getAvailableInvestableAmount(@PathVariable String campaignName) {
    return ResponseEntity.ok(campaignService.getAvailableInvestableAmount(campaignName));
  }

  @PostMapping(value = "/{campaignName}/invest")
  @PreAuthorize("hasAuthority('ROLE_INVESTOR')")
  public ResponseEntity investInCampaign(
      @RequestBody AvailableInvestmentDto amountOfMoney, @PathVariable String campaignName) {
    investmentService.invest(amountOfMoney.getAmount(), campaignName);
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAuthority('ROLE_INVESTOR')")
  @GetMapping(value = "/mine/portfolio")
  public ResponseEntity<Page<PortfolioCampaignResponseDto>> getPortfolio(
      Pageable pageable,
      @RequestParam(value = "filter", required = false, defaultValue = "all") String filter) {
    return ResponseEntity.ok(investmentService.getPortfolio(pageable, filter));
  }

  @PostMapping(value = "/{campaignName}/updates")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignUpdateResponseDto> createCampaignUpdate(
      @PathVariable String campaignName, @Valid @RequestBody CampaignUpdateDto campaignUpdateDto) {
    return ResponseEntity.ok(
        campaignUpdateService.createCampaignUpdate(campaignName, campaignUpdateDto));
  }

  @PutMapping(value = "/mine/updates/{campaignUpdateId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignUpdateResponseDto> updateCampaignUpdate(
      @PathVariable Long campaignUpdateId,
      @Valid @RequestBody CampaignUpdateDto campaignUpdateDto) {
    return ResponseEntity.ok(
        campaignUpdateService.updateCampaignUpdate(campaignUpdateId, campaignUpdateDto));
  }

  @PostMapping(value = "/mine/updates/{campaignUpdateId}/images")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<FilenameDto> uploadCampaignUpdateImage(
      @PathVariable Long campaignUpdateId, @RequestParam("image") MultipartFile image) {
    return ResponseEntity.ok(campaignUpdateImageService.uploadImage(campaignUpdateId, image));
  }

  @DeleteMapping(value = "/mine/updates/{campaignUpdateId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity deleteCampaignUpdate(@PathVariable Long campaignUpdateId) {
    campaignUpdateService.deleteCampaignUpdate(campaignUpdateId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/updates/{campaignUpdateId}")
  public ResponseEntity<CampaignUpdateResponseDto> getCampaignUpdate(
      @PathVariable Long campaignUpdateId) {
    return ResponseEntity.ok(campaignUpdateService.getCampaignUpdate(campaignUpdateId));
  }

  @GetMapping(value = "/updates")
  @PreAuthorize("hasAuthority('ROLE_INVESTOR')")
  public ResponseEntity<Page<CampaignUpdateResponseDto>> getCampaignUpdates(
      Pageable pageable,
      @RequestParam(value = "filter", required = false, defaultValue = "all") String filter) {
    return ResponseEntity.ok(campaignUpdateService.getCampaignUpdates(pageable, filter));
  }

  @Override
  @GetMapping(value = "/{campaignName}/updates")
  public ResponseEntity listCampaignsUpdates(Pageable pageable, @PathVariable String campaignName) {
    return ResponseEntity.ok(
        campaignUpdateService.getCampaignUpdatesForCampaign(campaignName, pageable));
  }
}
