package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.AuditAssignmentDto;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.Campaign;

public interface AuditService {

  Audit assignAudit(AuditAssignmentDto auditAssignmentDto);

  Audit acceptCampaign(Long auditId);

  void sendAcceptCampaignEmail(Audit audit);

  Audit declineCampaign(Long auditId, String rejectionReason);

  void sendDeclineCampaignEmail(Audit audit);

  Audit findPendingAuditByCampaignOrThrowException(Campaign campaign);
}
