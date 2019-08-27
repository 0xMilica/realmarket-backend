package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDocumentAccessRequestDto extends AbstractBlockchainDto {
  private Long campaignDocumentAccessRequestId;
  private Long campaignId;
  private String requestState;

  public CampaignDocumentAccessRequestDto(
      CampaignDocumentsAccessRequest campaignDocumentAccessRequest, Long userId) {
    this.campaignDocumentAccessRequestId = campaignDocumentAccessRequest.getId();
    this.campaignId = campaignDocumentAccessRequest.getCampaign().getId();
    this.requestState = campaignDocumentAccessRequest.getRequestState().getName().toString();
    this.userId = userId;
  }
}
