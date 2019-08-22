package io.realmarket.propeler.service.blockchain.dto.investment.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDetails {
  private Long paymentId;
  private Long investmentId;
  private Double amount;
  private String creationDate;
  private String paymentDate;
  private Long adminId;
  private String paymentType;
  private String sessionToken;
  private String accountNumber;
  private String routingNumber;
  private String proformaInvoiceUrl;
  private String currency;
}
