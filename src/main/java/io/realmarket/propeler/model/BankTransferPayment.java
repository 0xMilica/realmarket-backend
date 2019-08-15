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
@DiscriminatorValue("BankTransferPayment")
@Entity(name = "BankTransferPayment")
public class BankTransferPayment extends Payment {

  private String accountNumber;

  private String routingNumber;

  private String proformaInvoiceUrl;

  @Builder(builderMethodName = "bankTransferPaymentBuilder")
  public BankTransferPayment(
      Investment investment,
      BigDecimal amount,
      String currency,
      Instant creationDate,
      Instant paymentDate,
      String accountNumber,
      String routingNumber,
      String proformaInvoiceUrl) {
    super(investment, amount, currency, creationDate, paymentDate);
    this.accountNumber = accountNumber;
    this.routingNumber = routingNumber;
    this.proformaInvoiceUrl = proformaInvoiceUrl;
  }
}
