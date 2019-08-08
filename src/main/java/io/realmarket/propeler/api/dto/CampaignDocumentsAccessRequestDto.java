package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.enums.UserRoleName;
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

  @ApiModelProperty(value = "Auth identifier of user who sent request")
  private Long authId;

  @ApiModelProperty(value = "Name of user (individual/corporate) who sent request")
  private String name;

  @ApiModelProperty(value = "Request state")
  private String requestState;

  public CampaignDocumentsAccessRequestDto(
      CampaignDocumentsAccessRequest campaignDocumentsAccessRequest) {
    this.requestId = campaignDocumentsAccessRequest.getId();
    this.campaignUrlFriendlyName =
        campaignDocumentsAccessRequest.getCampaign().getUrlFriendlyName();
    Auth auth = campaignDocumentsAccessRequest.getAuth();
    this.authId = auth.getId();
    Person person = auth.getPerson();
    if (auth.getUserRole().getName().equals(UserRoleName.ROLE_COMPANY_INVESTOR)) {
      this.name = person.getCompanyName();
    } else {
      this.name = person.getFirstName() + " " + person.getLastName();
    }
    this.requestState = campaignDocumentsAccessRequest.getRequestState().getName().toString();
  }
}
