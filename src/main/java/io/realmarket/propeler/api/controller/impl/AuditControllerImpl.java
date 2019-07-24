package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.AuditController;
import io.realmarket.propeler.api.dto.AuditAssignmentDto;
import io.realmarket.propeler.api.dto.AuditDeclineDto;
import io.realmarket.propeler.api.dto.AuditResponseDto;
import io.realmarket.propeler.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/audits")
@Slf4j
public class AuditControllerImpl implements AuditController {

  private AuditService auditService;

  @Autowired
  public AuditControllerImpl(AuditService auditService) {
    this.auditService = auditService;
  }

  @Override
  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<AuditResponseDto> assignAudit(
      @RequestBody @Valid AuditAssignmentDto auditAssignmentDto) {
    return ResponseEntity.ok(new AuditResponseDto(auditService.assignAudit(auditAssignmentDto)));
  }

  @Override
  @PatchMapping(value = "/{auditId}/accept")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<AuditResponseDto> acceptAudit(@PathVariable Long auditId) {
    return ResponseEntity.ok(new AuditResponseDto(auditService.acceptCampaign(auditId)));
  }

  @Override
  @PatchMapping(value = "/{auditId}/decline")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<AuditResponseDto> declineAudit(
      @PathVariable Long auditId, @RequestBody AuditDeclineDto auditDeclineDto) {
    return ResponseEntity.ok(
        new AuditResponseDto(auditService.declineCampaign(auditId, auditDeclineDto.getContent())));
  }
}
