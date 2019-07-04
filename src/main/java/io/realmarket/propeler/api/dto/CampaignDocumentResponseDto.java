package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.CampaignDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@ApiModel(value = "CampaignDocumentResponseDto")
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDocumentResponseDto extends CampaignDocumentDto {

  @ApiModelProperty(value = "Campaign document's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Campaign document's upload date")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant uploadDate;

  @ApiModelProperty(value = "Campaign name")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String campaignName;

  public CampaignDocumentResponseDto(CampaignDocument campaignDocument) {
    super(campaignDocument);
    this.uploadDate = campaignDocument.getUploadDate();
    this.id = campaignDocument.getId();
    this.campaignName = campaignDocument.getCampaign().getName();
  }
}
