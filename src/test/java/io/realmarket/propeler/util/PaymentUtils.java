package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.model.BankTransferPayment;
import io.realmarket.propeler.model.Investment;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentUtils {

  public static final Long TEST_ID = 1L;
  public static final BigDecimal TEST_AMOUNT = BigDecimal.valueOf(1000);
  public static final String TEST_CURRENCY = "RSD";
  public static final Instant TEST_CREATION_DATE = Instant.now().minusMillis(700000000);
  public static final Instant TEST_PAYMENT_DATE = Instant.now();
  public static final String TEST_ACCOUNT_NUMBER = "123456789";
  public static final String TEST_ROUTING_NUMBER = "1234";
  public static final String TEST_PROFORMA_INVOICE_URL = "TEST_PROFORMA_INVOICE_URL";

  public static BankTransferPayment mockPaidBankTransferPayment() {
    Investment ownerApprovedInvestment = InvestmentUtils.mockOwnerApprovedInvestment();

    return BankTransferPayment.bankTransferPaymentBuilder()
        .investment(ownerApprovedInvestment)
        .amount(TEST_AMOUNT)
        .currency(TEST_CURRENCY)
        .creationDate(TEST_CREATION_DATE)
        .paymentDate(TEST_PAYMENT_DATE)
        .accountNumber(TEST_ACCOUNT_NUMBER)
        .routingNumber(TEST_ROUTING_NUMBER)
        .build();
  }

  public static final PaymentConfirmationDto TEST_PAYMENT_CONFIRMATION_DTO =
      PaymentConfirmationDto.builder()
          .documentUrl("TEST_URL")
          .documentTitle("TEST_TITLE")
          .paymentDate("01.01.2020.")
          .build();
}
