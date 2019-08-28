package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PayPalPayment")
@Entity(name = "PayPalPayment")
public class PayPalPayment extends Payment {

  private String payPalOrderId;

  @Builder(builderMethodName = "payPalPaymentBuilder")
  public PayPalPayment(
      Investment investment,
      BigDecimal amount,
      String currency,
      Instant creationDate,
      Instant paymentDate,
      String payPalOrderId) {
    super(investment, amount, currency, creationDate, paymentDate);
    this.payPalOrderId = payPalOrderId;
  }
}
