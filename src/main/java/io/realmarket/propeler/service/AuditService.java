package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.AuditRequestDto;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.Campaign;

public interface AuditService {

  Audit assignAudit(AuditRequestDto auditRequestDto);

  Audit acceptCampaign(Long auditId);

  void sendAcceptCampaignEmail(Audit audit);

  Audit declineCampaign(Long auditId, String content);

  void sendDeclineCampaignEmail(Audit audit);

  Audit findPendingAuditByCampaignOrThrowException(Campaign campaign);
}
