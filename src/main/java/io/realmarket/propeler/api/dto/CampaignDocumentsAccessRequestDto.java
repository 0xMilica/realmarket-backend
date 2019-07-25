package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "Campaign documents access request identifier")
  private Long requestId;

  @ApiModelProperty(value = "Campaign url friendly name")
  private String campaignUrlFriendlyName;

  @ApiModelProperty(value = "Auth identifier of user who send request")
  private Long authId;

  @ApiModelProperty(value = "Name of user who send request")
  private String personName;

  @ApiModelProperty(value = "Request state")
  private String requestState;

  public CampaignDocumentsAccessRequestDto(
      CampaignDocumentsAccessRequest campaignDocumentsAccessRequest) {
    this.requestId = campaignDocumentsAccessRequest.getId();
    this.campaignUrlFriendlyName =
        campaignDocumentsAccessRequest.getCampaign().getUrlFriendlyName();
    this.authId = campaignDocumentsAccessRequest.getAuth().getId();
    this.personName =
        campaignDocumentsAccessRequest.getAuth().getPerson().getFirstName()
            + " "
            + campaignDocumentsAccessRequest.getAuth().getPerson().getLastName();
    this.requestState = campaignDocumentsAccessRequest.getRequestState().getName().toString();
  }
}
