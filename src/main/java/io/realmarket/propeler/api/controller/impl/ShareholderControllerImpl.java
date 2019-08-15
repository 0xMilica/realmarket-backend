package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.ShareholderController;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.ShareholderPublicResponseDto;
import io.realmarket.propeler.api.dto.ShareholderRequestDto;
import io.realmarket.propeler.api.dto.ShareholderResponseDto;
import io.realmarket.propeler.service.ShareholderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<ShareholderResponseDto> createShareholder(
      @RequestBody ShareholderRequestDto shareholderRequestDto) {
    return new ResponseEntity<>(
        new ShareholderResponseDto(shareholderService.createShareholder(shareholderRequestDto)),
        HttpStatus.CREATED);
  }

  @Override
  @PatchMapping("/mine/shareholders")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<List<ShareholderResponseDto>> patchShareholderOrder(
      @RequestBody List<Long> shareholderOrder) {
    return ResponseEntity.ok(
        shareholderService.patchShareholderOrder(shareholderOrder).stream()
            .map(ShareholderResponseDto::new)
            .collect(Collectors.toList()));
  }

  @Override
  @GetMapping("/mine/shareholders")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<List<ShareholderResponseDto>> getShareholders() {
    return ResponseEntity.ok(
        shareholderService.getShareholders().stream()
            .map(ShareholderResponseDto::new)
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
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<ShareholderResponseDto> patchShareholder(
      @PathVariable Long shareholderId, @RequestBody ShareholderRequestDto shareholderRequestDto) {
    return ResponseEntity.ok(
        new ShareholderResponseDto(shareholderService.patchShareholder(shareholderId, shareholderRequestDto)));
  }

  @Override
  @DeleteMapping("/mine/shareholders/{shareholderId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity deleteShareholder(@PathVariable Long shareholderId) {
    shareholderService.deleteShareholder(shareholderId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/mine/shareholders/{shareholderId}/picture")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity uploadShareholderPicture(
      @PathVariable Long shareholderId, @RequestParam("picture") MultipartFile picture) {
    shareholderService.uploadPicture(shareholderId, picture);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Override
  @GetMapping("/mine/shareholders/{shareholderId}/picture")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<FileDto> downloadShareholderPicture(@PathVariable Long shareholderId) {
    return ResponseEntity.ok(shareholderService.downloadPicture(shareholderId));
  }

  @Override
  @GetMapping("/{companyId}/shareholders/{shareholderId}/picture")
  public ResponseEntity<FileDto> downloadShareholderPicture(
      @PathVariable Long companyId, @PathVariable Long shareholderId) {
    return ResponseEntity.ok(shareholderService.downloadPublicPicture(companyId, shareholderId));
  }

  @Override
  @DeleteMapping("/mine/shareholders/{shareholderId}/picture")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity deleteShareholderPicture(@PathVariable Long shareholderId) {
    shareholderService.deletePicture(shareholderId);
    return ResponseEntity.noContent().build();
  }
}
