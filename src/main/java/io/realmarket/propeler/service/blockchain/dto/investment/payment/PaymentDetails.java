package io.realmarket.propeler.service.blockchain.dto.investment.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
  private Long paymentId;
  private Long investmentId;
  private Double amount;
  private String creationDate;
  private String paymentDate;
  private Long adminId;
  private String paymentType;
  private String orderId;
  private String sessionToken;
  private String accountNumber;
  private String routingNumber;
  private String proformaInvoiceUrl;
  private String currency;
}
