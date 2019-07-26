package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Data;

@Data
public class CampaignDocumentAccessRequestDto extends AbstractBlockchainDto {
  private Long campaignDocumentAccessRequestId;
  private Long campaignId;
  private String requestState;

  public CampaignDocumentAccessRequestDto(
      CampaignDocumentsAccessRequest campaignDocumentAccessRequest, Long userId) {
    this.campaignDocumentAccessRequestId = campaignDocumentAccessRequest.getId();
    this.campaignId = campaignDocumentAccessRequest.getCampaign().getId();
    this.requestState = campaignDocumentAccessRequest.getRequestState().toString();
    this.userId = userId;
  }
}
