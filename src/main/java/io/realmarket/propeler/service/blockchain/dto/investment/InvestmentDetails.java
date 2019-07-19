package io.realmarket.propeler.service.blockchain.dto.investment;

import io.realmarket.propeler.model.Investment;
import lombok.Data;

@Data
public class InvestmentDetails {
  private Long investmentId;
  private Long investorId;
  private Long campaignId;
  private String investmentState;
  private Double investedAmount;
  private String creationDate;

  public InvestmentDetails(Investment investment) {
    this.investmentId = investment.getId();
    this.investorId = investment.getPerson().getId();
    this.campaignId = investment.getCampaign().getId();
    this.investedAmount = investment.getInvestedAmount().doubleValue();
    this.investmentState = investment.getInvestmentState().getName().toString();
    this.creationDate = investment.getCreationDate().toString();
  }
}
