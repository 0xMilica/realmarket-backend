package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Campaign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(description = " CampaignWithInvestmentsResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignWithInvestmentsWithPersonResponseDto {

  @ApiModelProperty(value = "Campaign information")
  private CampaignResponseDto campaign;

  @ApiModelProperty(value = "Campaign investments")
  private List<InvestmentWithPersonResponseDto> investments;

  public CampaignWithInvestmentsWithPersonResponseDto(
      Campaign campaign, List<InvestmentWithPersonResponseDto> investments) {
    this.campaign = new CampaignResponseDto(campaign);
    this.investments = investments;
  }
}
