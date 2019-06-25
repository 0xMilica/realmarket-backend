package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.CampaignUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@ApiModel(value = "CampaignUpdateResponseDto")
@NoArgsConstructor
@AllArgsConstructor
public class CampaignUpdateResponseDto extends CampaignUpdateDto {

  @ApiModelProperty(value = "Campaign update identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Campaign update post date")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant postDate;

  @ApiModelProperty(value = "Campaign name")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String campaignName;

  @ApiModelProperty(value = "Campaign url friendly name")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String campaignUrlFriendlyName;

  public CampaignUpdateResponseDto(CampaignUpdate campaignUpdate) {
    super(campaignUpdate);
    this.id = campaignUpdate.getId();
    this.postDate = campaignUpdate.getPostDate();
    this.campaignName = campaignUpdate.getCampaign().getName();
    this.campaignUrlFriendlyName = campaignUpdate.getCampaign().getUrlFriendlyName();
  }
}
