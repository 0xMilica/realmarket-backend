package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.ShareholderController;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.ShareholderDto;
import io.realmarket.propeler.api.dto.ShareholderPublicResponseDto;
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
@RequestMapping(value = "/companies")
@Slf4j
public class ShareholderControllerImpl implements ShareholderController {

  private final ShareholderService shareholderService;

  @Autowired
  public ShareholderControllerImpl(ShareholderService shareholderService) {
    this.shareholderService = shareholderService;
  }

  @Override
  @PostMapping("/mine/shareholders")
  public ResponseEntity<ShareholderDto> createShareholder(
      @RequestBody ShareholderDto shareholderDto) {
    return new ResponseEntity<>(
        new ShareholderDto(shareholderService.createShareholder(shareholderDto)),
        HttpStatus.CREATED);
  }

  @Override
  @PatchMapping("/mine/shareholders")
  public ResponseEntity<List<ShareholderDto>> patchShareholderOrder(
      @RequestBody List<Long> shareholderOrder) {
    return ResponseEntity.ok(
        shareholderService.patchShareholderOrder(shareholderOrder).stream()
            .map(ShareholderDto::new)
            .collect(Collectors.toList()));
  }

  @Override
  @GetMapping("/mine/shareholders")
  public ResponseEntity<List<ShareholderDto>> getShareholders() {
    return ResponseEntity.ok(
        shareholderService.getShareholders().stream()
            .map(ShareholderDto::new)
            .collect(Collectors.toList()));
  }

  @Override
  @GetMapping("/{companyId}/shareholders")
  public ResponseEntity<List<ShareholderPublicResponseDto>> getShareholders(
      @PathVariable Long companyId) {
    return ResponseEntity.ok(
        shareholderService.getShareholders(companyId).stream()
            .map(ShareholderPublicResponseDto::new)
            .collect(Collectors.toList()));
  }

  @Override
  @PatchMapping("/mine/shareholders/{shareholderId}")
  public ResponseEntity<ShareholderDto> patchShareholder(
      @PathVariable Long shareholderId, @RequestBody ShareholderDto shareholderDto) {
    return ResponseEntity.ok(
        new ShareholderDto(shareholderService.patchShareholder(shareholderId, shareholderDto)));
  }

  @Override
  @DeleteMapping("/mine/shareholders/{shareholderId}")
  public ResponseEntity deleteShareholder(@PathVariable Long shareholderId) {
    shareholderService.deleteShareholder(shareholderId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/mine/shareholders/{shareholderId}/picture")
  public ResponseEntity uploadShareholderPicture(
      @PathVariable Long shareholderId, @RequestParam("picture") MultipartFile picture) {
    shareholderService.uploadPicture(shareholderId, picture);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Override
  @GetMapping("/mine/shareholders/{shareholderId}/picture")
  public ResponseEntity<FileDto> downloadShareholderPicture(@PathVariable Long shareholderId) {
    return ResponseEntity.ok(shareholderService.downloadPicture(shareholderId));
  }

  @Override
  @GetMapping("/{companyId}/shareholders/{shareholderId}/picture")
  public ResponseEntity<FileDto> downloadShareholderPicture(
      @PathVariable Long companyId, @PathVariable Long shareholderId) {
    return ResponseEntity.ok(shareholderService.downloadPicture(shareholderId));
  }

  @Override
  @DeleteMapping("/mine/shareholders/{shareholderId}/picture")
  public ResponseEntity deleteShareholderPicture(@PathVariable Long shareholderId) {
    shareholderService.deletePicture(shareholderId);
    return ResponseEntity.noContent().build();
  }
}
