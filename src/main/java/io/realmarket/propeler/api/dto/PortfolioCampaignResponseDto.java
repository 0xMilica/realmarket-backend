package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(description = "PortfolioCampaignResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioCampaignResponseDto {

  @ApiModelProperty(value = "Campaign information")
  private CampaignResponseDto campaign;

  @ApiModelProperty(value = "Campaign investments")
  private List<CampaignInvestmentResponseDto> investments;

  @ApiModelProperty(value = "Total investments information")
  private TotalCampaignInvestmentsResponseDto total;
}
