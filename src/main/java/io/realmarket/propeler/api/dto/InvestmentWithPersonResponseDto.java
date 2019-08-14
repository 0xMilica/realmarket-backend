package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Investment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@ApiModel(description = "CampaignPortfolioResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentWithPersonResponseDto {

  @ApiModelProperty(value = "Investment identifier")
  private Long id;

  @ApiModelProperty(value = "Invested amount")
  private BigDecimal investedAmount;

  @ApiModelProperty(value = "Investment equity")
  private BigDecimal equity;

  @ApiModelProperty(value = "Payment date")
  private Instant paymentDate;

  @ApiModelProperty(value = "Investment state")
  private String investmentState;

  private PersonResponseDto person;

  public InvestmentWithPersonResponseDto(Investment investment, PersonResponseDto person) {
    this.id = investment.getId();
    this.investedAmount = investment.getInvestedAmount();
    this.equity =
        investment
            .getInvestedAmount()
            .multiply(investment.getCampaign().getMinEquityOffered())
            .divide(
                BigDecimal.valueOf(investment.getCampaign().getFundingGoals()),
                4,
                RoundingMode.DOWN);
    this.paymentDate = investment.getPaymentDate();
    this.investmentState = investment.getInvestmentState().getName().toString();
    this.person = person;
  }
}
