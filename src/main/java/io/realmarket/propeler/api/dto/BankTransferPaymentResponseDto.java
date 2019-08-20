package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.BankTransferPayment;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@ApiModel(value = "BankTransferPaymentResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankTransferPaymentResponseDto {

  Long bankTransferPaymentId;

  Long investmentId;

  BigDecimal amount;

  // TODO: Bind this field to currency entity later
  String currency;

  Instant creationDate;

  Instant paymentDate;

  String accountNumber;

  String routingNumber;

  String proformaInvoiceUrl;

  public BankTransferPaymentResponseDto(BankTransferPayment bankTransferPayment) {
    this.bankTransferPaymentId = bankTransferPayment.getId();
    this.investmentId = bankTransferPayment.getInvestment().getId();
    this.amount = bankTransferPayment.getAmount();
    this.currency = bankTransferPayment.getCurrency();
    this.creationDate = bankTransferPayment.getCreationDate();
    this.paymentDate = bankTransferPayment.getPaymentDate();
    this.accountNumber = bankTransferPayment.getAccountNumber();
    this.routingNumber = bankTransferPayment.getRoutingNumber();
    this.proformaInvoiceUrl = bankTransferPayment.getProformaInvoiceUrl();
  }
}
