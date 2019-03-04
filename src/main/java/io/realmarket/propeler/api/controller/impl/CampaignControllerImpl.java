package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CampaignController;
import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/campaigns")
public class CampaignControllerImpl implements CampaignController {

  private final CampaignService campaignService;

  @Autowired
  public CampaignControllerImpl(CampaignService campaignService) {
    this.campaignService = campaignService;
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
    return ResponseEntity.ok(
        new CampaignDto(campaignService.findByUrlFriendlyNameOrThrowException(campaignName)));
  }

  @PatchMapping(value = "/{campaignName}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignDto> patchCampaign(
      @PathVariable String campaignName, @RequestBody @Valid CampaignPatchDto campaignPatchDto) {
    return ResponseEntity.ok(campaignService.patchCampaign(campaignName, campaignPatchDto));
  }

  @PostMapping("/{campaignName}/market_image")
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
  public ResponseEntity deleteMarketImage(@PathVariable String campaignName) {
    campaignService.deleteMarketImage(campaignName);
    return ResponseEntity.noContent().build();
  }
}
