package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CompanyController;
import io.realmarket.propeler.api.dto.CompanyDto;
import io.realmarket.propeler.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping(value = "/{id}")
  public ResponseEntity<CompanyDto> getCompany(@PathVariable Long id) {
    return new ResponseEntity<>(
        new CompanyDto(companyService.findByIdOrThrowException(id)), HttpStatus.OK);
  }
}
