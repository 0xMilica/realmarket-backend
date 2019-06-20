package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CampaignInvestment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ApiModel(description = "CampaignPortfolioResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignInvestmentResponseDto {

  @ApiModelProperty(value = "Campaign investment identifier")
  private Long id;

  @ApiModelProperty(value = "Invested amount")
  private BigDecimal investedAmount;

  @ApiModelProperty(value = "Campaign investment equity")
  private BigDecimal equity;

  public CampaignInvestmentResponseDto(CampaignInvestment investment) {
    this.id = investment.getId();
    this.investedAmount = investment.getInvestedAmount();
    this.equity =
        investment
            .getInvestedAmount()
            .multiply(investment.getCampaign().getMinEquityOffered())
            .divide(
                BigDecimal.valueOf(investment.getCampaign().getFundingGoals()),
                2,
                RoundingMode.FLOOR);
  }
}
