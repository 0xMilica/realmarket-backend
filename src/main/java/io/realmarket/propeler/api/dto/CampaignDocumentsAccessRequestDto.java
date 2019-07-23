package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "CampaignDocumentsAccessRequestResponseDto")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CampaignDocumentsAccessRequestDto {

  private Long requestId;
  private String campaignUrlFriendlyName;
  private Long authId;
  private String requestState;

  public CampaignDocumentsAccessRequestDto(
      CampaignDocumentsAccessRequest campaignDocumentsAccessRequest) {
    this.requestId = campaignDocumentsAccessRequest.getId();
    this.campaignUrlFriendlyName =
        campaignDocumentsAccessRequest.getCampaign().getUrlFriendlyName();
    this.authId = campaignDocumentsAccessRequest.getAuth().getId();
    this.requestState = campaignDocumentsAccessRequest.getRequestState().getName().toString();
  }
}
