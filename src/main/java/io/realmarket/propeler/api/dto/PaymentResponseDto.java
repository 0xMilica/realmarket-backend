package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.Payment;
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

  private String invoiceUrl;

  public PaymentResponseDto(Investment investment) {
    this.investmentId = investment.getId();
    this.amount = investment.getInvestedAmount();
    this.creationDate = investment.getCreationDate();
    this.paymentDate = investment.getPaymentDate();
    this.paymentState = investment.getInvestmentState().getName().toString();
    if (investment.getPerson().getCompanyName() == null) {
      this.investorName =
          investment.getPerson().getFirstName() + " " + investment.getPerson().getLastName();
    } else {
      this.investorName = investment.getPerson().getCompanyName();
    }
    this.campaignName = investment.getCampaign().getName();
    this.invoiceUrl = investment.getInvoiceUrl();
  }

  public PaymentResponseDto(Payment payment) {
    this.investmentId = payment.getInvestment().getId();
    this.amount = payment.getAmount();
    this.creationDate = payment.getCreationDate();
    this.paymentDate = payment.getPaymentDate();
    this.paymentState = payment.getInvestment().getInvestmentState().getName().toString();
    if (payment.getInvestment().getPerson().getCompanyName() == null) {
      this.investorName =
          payment.getInvestment().getPerson().getFirstName()
              + " "
              + payment.getInvestment().getPerson().getLastName();
    } else {
      this.investorName = payment.getInvestment().getPerson().getCompanyName();
    }
    this.campaignName = payment.getInvestment().getCampaign().getName();
    this.invoiceUrl = payment.getInvestment().getInvoiceUrl();
  }
}
