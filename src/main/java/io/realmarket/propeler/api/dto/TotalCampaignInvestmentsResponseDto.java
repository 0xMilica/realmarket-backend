package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CampaignInvestment;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ApiModel(description = "TotalCampaignInvestmentResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalCampaignInvestmentsResponseDto {

  private BigDecimal amount;
  private BigDecimal equity;

  public TotalCampaignInvestmentsResponseDto(List<CampaignInvestment> investments) {
    this.amount = BigDecimal.valueOf(0);
    this.equity = BigDecimal.valueOf(0);
    investments.forEach(
        investment -> {
          this.amount = this.amount.add(investment.getInvestedAmount());
          this.equity =
              this.equity.add(
                  investment
                      .getInvestedAmount()
                      .multiply(investment.getCampaign().getMinEquityOffered())
                      .divide(
                          BigDecimal.valueOf(investment.getCampaign().getFundingGoals()),
                          2,
                          RoundingMode.FLOOR));
        });
  }
}
