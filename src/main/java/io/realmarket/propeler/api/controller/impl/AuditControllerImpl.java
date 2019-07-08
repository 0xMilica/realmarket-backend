package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.AuditController;
import io.realmarket.propeler.api.dto.AuditDeclineDto;
import io.realmarket.propeler.api.dto.AuditResponseDto;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audits")
public class AuditControllerImpl implements AuditController {

  private final AuditService auditService;

  @Autowired
  public AuditControllerImpl(AuditService auditService) {
    this.auditService = auditService;
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
  public ResponseEntity<AuditResponseDto> declineAudit(@PathVariable Long auditId, AuditDeclineDto auditDeclineDto) {
    return ResponseEntity.ok(new AuditResponseDto(auditService.declineCampaign(auditId, auditDeclineDto.getContent())));
  }
}
