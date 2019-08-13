package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Investment;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@ApiModel(description = "PaymentResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

  private Long investmentId;

  private BigDecimal amount;

  private Instant creationDate;

  private Instant paymentDate;

  private String paymentState;

  private String investorName;

  private String campaignName;

  public PaymentResponseDto(Investment investment) {
    this.investmentId = investment.getId();
    this.amount = investment.getInvestedAmount();
    this.creationDate = investment.getCreationDate();
    this.paymentDate = investment.getPaymentDate();
    this.paymentState = investment.getInvestmentState().toString();
    if (investment.getPerson().getCompanyName() == null) {
      this.investorName =
          investment.getPerson().getFirstName() + " " + investment.getPerson().getLastName();
    } else {
      this.investorName = investment.getPerson().getCompanyName();
    }
    this.campaignName = investment.getCampaign().getName();
  }
}
