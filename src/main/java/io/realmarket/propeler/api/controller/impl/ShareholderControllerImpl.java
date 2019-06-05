package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.ShareholderController;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.ShareholderDto;
import io.realmarket.propeler.service.ShareholderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/campaigns/{campaignName}/shareholders")
@Slf4j
public class ShareholderControllerImpl implements ShareholderController {

  private final ShareholderService shareholderService;

  @Autowired
  public ShareholderControllerImpl(ShareholderService shareholderService) {
    this.shareholderService = shareholderService;
  }

  @Override
  @PostMapping
  public ResponseEntity<ShareholderDto> createShareholder(
      @PathVariable String campaignName, @RequestBody ShareholderDto shareholderDto) {
    return new ResponseEntity<>(
        new ShareholderDto(shareholderService.createShareholder(campaignName, shareholderDto)),
        HttpStatus.CREATED);
  }

  @Override
  @PatchMapping
  public ResponseEntity<List<ShareholderDto>> patchShareholderOrder(
      @PathVariable String campaignName, @RequestBody List<Long> shareholderOrder) {
    return ResponseEntity.ok(
        shareholderService.patchShareholderOrder(campaignName, shareholderOrder).stream()
            .map(ShareholderDto::new)
            .collect(Collectors.toList()));
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ShareholderDto>> getShareholders(@PathVariable String campaignName) {
    return ResponseEntity.ok(
        shareholderService.getShareholders(campaignName).stream()
            .map(ShareholderDto::new)
            .collect(Collectors.toList()));
  }

  @Override
  @PatchMapping("/{shareholderId}")
  public ResponseEntity<ShareholderDto> patchShareholder(
      @PathVariable String campaignName,
      @PathVariable Long shareholderId,
      @RequestBody ShareholderDto shareholderDto) {
    return ResponseEntity.ok(
        new ShareholderDto(
            shareholderService.patchShareholder(campaignName, shareholderId, shareholderDto)));
  }

  @Override
  @DeleteMapping("/{shareholderId}")
  public ResponseEntity deleteShareholder(
      @PathVariable String campaignName, @PathVariable Long shareholderId) {
    shareholderService.deleteShareholder(campaignName, shareholderId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{shareholderId}/picture")
  public ResponseEntity uploadShareholderPicture(
      @PathVariable String campaignName,
      @PathVariable Long shareholderId,
      @RequestParam("picture") MultipartFile picture) {
    shareholderService.uploadPicture(campaignName, shareholderId, picture);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/{shareholderId}/picture")
  public ResponseEntity<FileDto> downloadShareholderPicture(
      @PathVariable String campaignName, @PathVariable Long shareholderId) {
    return ResponseEntity.ok(shareholderService.downloadPicture(campaignName, shareholderId));
  }

  @DeleteMapping("/{shareholderId}/picture")
  public ResponseEntity deleteShareholderPicture(
      @PathVariable String campaignName, @PathVariable Long shareholderId) {
    shareholderService.deletePicture(campaignName, shareholderId);
    return ResponseEntity.noContent().build();
  }
}
