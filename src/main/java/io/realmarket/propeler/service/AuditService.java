package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.AuditRequestDto;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.Campaign;

public interface AuditService {

  Audit findPendingAuditByCampaignOrThrowException(Campaign campaign);

  Audit assignAudit(AuditRequestDto auditRequestDto);

  Audit acceptCampaign(Long auditId);

  void sendAcceptCampaignEmail(Audit audit);

  Audit declineCampaign(Long auditId, String content);
}
