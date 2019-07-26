package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Data;

@Data
public class CampaignDocumentAccessRequestStateChangeDto extends AbstractBlockchainDto {
  private Long campaignDocumentAccessRequestId;
  private String newState;

  public CampaignDocumentAccessRequestStateChangeDto(
      CampaignDocumentsAccessRequest campaignDocumentAccessRequest, Long userId) {
    this.campaignDocumentAccessRequestId = campaignDocumentAccessRequest.getId();
    this.newState = campaignDocumentAccessRequest.getRequestState().getName().toString();
    this.userId = userId;
  }
}
