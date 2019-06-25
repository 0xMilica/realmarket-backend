package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CompanyController;
import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.CompanyDocumentService;
import io.realmarket.propeler.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/companies")
public class CompanyControllerImpl implements CompanyController {

  private final CompanyService companyService;
  private final CompanyDocumentService companyDocumentService;

  @Autowired
  public CompanyControllerImpl(
      CompanyService companyService, CompanyDocumentService companyDocumentService) {
    this.companyService = companyService;
    this.companyDocumentService = companyDocumentService;
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

  @GetMapping(value = "/mine")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CompanyDto> getMyCompany() {
    return new ResponseEntity<>(new CompanyDto(companyService.findMyCompany()), HttpStatus.OK);
  }

  @PatchMapping("/{companyId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CompanyDto> patchCompany(
      @PathVariable Long companyId, @RequestBody CompanyPatchDto companyPatchDto) {
    return new ResponseEntity<>(
        new CompanyDto(companyService.patch(companyId, companyPatchDto)),
        (companyPatchDto.shouldAdminBeCalled()) ? HttpStatus.NO_CONTENT : HttpStatus.ACCEPTED);
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

  @PostMapping(value = "/{companyId}/documents")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CompanyDocumentResponseDto> submitCompanyDocument(
      @RequestBody @Valid CompanyDocumentDto companyDocumentDto, @PathVariable Long companyId) {
    return ResponseEntity.ok(
        new CompanyDocumentResponseDto(
            companyDocumentService.submitDocument(companyDocumentDto, companyId)));
  }

  @PatchMapping(value = "/mine/documents/{documentId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CompanyDocumentResponseDto> patchCompanyDocument(
      @PathVariable Long documentId, @RequestBody @Valid CompanyDocumentDto companyDocumentDto) {
    return ResponseEntity.ok(
        new CompanyDocumentResponseDto(
            companyDocumentService.patchCompanyDocument(documentId, companyDocumentDto)));
  }

  @DeleteMapping(value = "/mine/documents/{documentId}")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity deleteCompanyDocument(@PathVariable Long documentId) {
    companyDocumentService.deleteDocument(documentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/{companyId}/documents")
  public ResponseEntity<List<CompanyDocumentResponseDto>> getCompanyDocuments(
      @PathVariable Long companyId) {
    return ResponseEntity.ok(companyDocumentService.getCompanyDocuments(companyId));
  }
}
