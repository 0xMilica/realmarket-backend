package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.PayPalPayment;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@ApiModel(value = "PayPalPaymentResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayPalPaymentResponseDto {

  Long payPalPaymentId;

  Long investmentId;

  BigDecimal amount;

  // TODO: Bind this field to currency entity later
  String currency;

  Instant creationDate;

  Instant paymentDate;

  String payPalOrderId;

  public PayPalPaymentResponseDto(PayPalPayment payPalPayment) {
    this.payPalPaymentId = payPalPayment.getId();
    this.investmentId = payPalPayment.getInvestment().getId();
    this.amount = payPalPayment.getAmount();
    this.currency = payPalPayment.getCurrency();
    this.creationDate = payPalPayment.getCreationDate();
    this.paymentDate = payPalPayment.getPaymentDate();
    this.payPalOrderId = payPalPayment.getPayPalOrderId();
  }
}
