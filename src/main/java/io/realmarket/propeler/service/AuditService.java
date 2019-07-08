package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.AuditRequestDto;
import io.realmarket.propeler.model.Audit;

public interface AuditService {

  Audit assignAudit(AuditRequestDto auditRequestDto);

  Audit acceptCampaign(Long auditId);

  Audit declineCampaign(Long auditId, String content);
}
