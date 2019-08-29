package io.realmarket.propeler.service.blockchain.dto.investment.payment;

import io.realmarket.propeler.model.BankTransferPayment;
import io.realmarket.propeler.model.PayPalPayment;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto extends AbstractBlockchainDto {
  private PaymentDetails payment;

  public PaymentDto(BankTransferPayment payment, Long adminId) {
    this.userId = adminId;
    this.payment =
        PaymentDetails.builder()
            .paymentId(payment.getId())
            .investmentId(payment.getInvestment().getId())
            .amount(payment.getAmount().doubleValue())
            .creationDate(payment.getCreationDate().toString())
            .paymentDate(payment.getPaymentDate().toString())
            .adminId(adminId)
            .paymentType("BankTransferPayment")
            .accountNumber(payment.getAccountNumber())
            .routingNumber(payment.getRoutingNumber())
            .proformaInvoiceUrl(payment.getProformaInvoiceUrl())
            .currency(payment.getCurrency())
            .build();
  }

  public PaymentDto(PayPalPayment payment, Long userId) {
    this.userId = userId;
    this.payment =
        PaymentDetails.builder()
            .paymentId(payment.getId())
            .investmentId(payment.getInvestment().getId())
            .amount(payment.getAmount().doubleValue())
            .creationDate(payment.getCreationDate().toString())
            .paymentDate(payment.getPaymentDate().toString())
            .paymentType("PayPalPayment")
            .orderId(payment.getPayPalOrderId())
            .currency(payment.getCurrency())
            .build();
  }
}
