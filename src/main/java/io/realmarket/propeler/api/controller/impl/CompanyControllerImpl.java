package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CompanyController;
import io.realmarket.propeler.api.dto.CompanyDto;
import io.realmarket.propeler.api.dto.CompanyPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/companies")
public class CompanyControllerImpl implements CompanyController {

  private final CompanyService companyService;

  @Autowired
  public CompanyControllerImpl(CompanyService companyService) {
    this.companyService = companyService;
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto) {
    return new ResponseEntity<>(
        new CompanyDto(companyService.save(companyDto.buildCompany())), HttpStatus.CREATED);
  }

  @PostMapping(value = "/{companyId}/logo")
  public ResponseEntity uploadLogo(
      @PathVariable Long companyId, @RequestParam("picture") MultipartFile picture) {
    companyService.uploadLogo(companyId, picture);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @GetMapping(value = "/{companyId}/logo")
  public ResponseEntity<FileDto> downloadLogo(@PathVariable Long companyId) {
    return ResponseEntity.ok(companyService.downloadLogo(companyId));
  }

  @DeleteMapping(value = "/{companyId}/logo")
  public ResponseEntity deleteLogo(@PathVariable Long companyId) {
    companyService.deleteLogo(companyId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/{companyId}")
  public ResponseEntity<CompanyDto> getCompany(@PathVariable Long companyId) {
    return new ResponseEntity<>(
        new CompanyDto(companyService.findByIdOrThrowException(companyId)), HttpStatus.OK);
  }

  @PatchMapping("/{companyId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CompanyDto> patchCompany(
      @PathVariable Long companyId, @RequestBody CompanyPatchDto companyPatchDto) {
    return ResponseEntity.ok(
        new CompanyDto(companyService.patch(companyId, companyPatchDto.buildCompany())));
  }

  @PostMapping(value = "/{companyId}/featured_image")
  public ResponseEntity uploadFeaturedImage(
      @PathVariable Long companyId, @RequestParam("picture") MultipartFile picture) {
    companyService.uploadFeaturedImage(companyId, picture);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @GetMapping(value = "/{companyId}/featured_image")
  public ResponseEntity<FileDto> downloadFeaturedImage(@PathVariable Long companyId) {
    return ResponseEntity.ok(companyService.downloadFeaturedImage(companyId));
  }

  @DeleteMapping(value = "/{companyId}/featured_image")
  public ResponseEntity deleteFeaturedImage(@PathVariable Long companyId) {
    companyService.deleteFeaturedImage(companyId);
    return ResponseEntity.noContent().build();
  }
}
