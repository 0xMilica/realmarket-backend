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
  private String investmentType;
  private Long adminId;

  public InvestmentDetails(Investment investment) {
    setCommonFields(investment);
    this.investmentType = "regular";
  }

  public InvestmentDetails(Investment investment, Long adminId) {
    setCommonFields(investment);
    this.adminId = adminId;
    this.investmentType = "offPlatform";
  }

  private void setCommonFields(Investment investment) {
    this.investmentId = investment.getId();
    this.investorId = investment.getPerson().getId();
    this.campaignId = investment.getCampaign().getId();
    this.investedAmount = investment.getInvestedAmount().doubleValue();
    this.investmentState = investment.getInvestmentState().getName().toString();
    this.creationDate = investment.getCreationDate().toString();
  }
}
