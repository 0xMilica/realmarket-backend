package io.realmarket.propeler.util;

import io.realmarket.propeler.model.DocumentAccessLevel;
import io.realmarket.propeler.model.DocumentType;
import io.realmarket.propeler.model.Payment;
import io.realmarket.propeler.model.PaymentDocument;
import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import io.realmarket.propeler.model.enums.DocumentTypeName;

public class PaymentDocumentUtils {

  public static final Long TEST_ID = 1L;
  public static final String TEST_TITLE = "TEST_TITLE";
  public static final String TEST_URL = "TEST_URL";
  public static final DocumentAccessLevelName TEST_ACCESS_LEVEL_ENUM =
      DocumentAccessLevelName.PRIVATE;
  public static final DocumentTypeName TEST_TYPE_ENUM = DocumentTypeName.PAYMENT_PROOF;
  public static final DocumentAccessLevel TEST_ACCESS_LEVEL =
      DocumentAccessLevel.builder().name(TEST_ACCESS_LEVEL_ENUM).build();
  public static final DocumentType TEST_TYPE = DocumentType.builder().name(TEST_TYPE_ENUM).build();

  public static PaymentDocument mockPaymentDocument() {
    Payment payment = PaymentUtils.mockPaidBankTransferPayment();

    return PaymentDocument.paymentDocumentBuilder()
        .title(TEST_TITLE)
        .accessLevel(TEST_ACCESS_LEVEL)
        .type(TEST_TYPE)
        .url(TEST_URL)
        .payment(payment)
        .build();
  }
}
