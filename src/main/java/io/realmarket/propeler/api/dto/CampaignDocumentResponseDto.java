package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.CampaignDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@ApiModel(value = "CampaignDocumentResponseDto")
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDocumentResponseDto extends CampaignDocumentDto {

  @ApiModelProperty(value = "Campaign document's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Campaign document's campaign")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private CampaignDto campaign;

  public CampaignDocumentResponseDto(CampaignDocument campaignDocument) {
    super(campaignDocument);
    this.id = campaignDocument.getId();
    this.campaign = new CampaignDto(campaignDocument.getCampaign());
  }
}
