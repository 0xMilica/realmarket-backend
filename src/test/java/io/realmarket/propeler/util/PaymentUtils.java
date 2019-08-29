package io.realmarket.propeler.util;

import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.Order;
import com.paypal.orders.PurchaseUnit;
import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.model.BankTransferPayment;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.PayPalPayment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentUtils {

  public static final Long TEST_ID = 1L;
  public static final BigDecimal TEST_AMOUNT = BigDecimal.valueOf(1000);
  public static final String TEST_CURRENCY = "EUR";
  public static final Instant TEST_CREATION_DATE = Instant.now().minusMillis(700000000);
  public static final Instant TEST_PAYMENT_DATE = Instant.now();
  public static final String TEST_ACCOUNT_NUMBER = "123456789";
  public static final String TEST_ROUTING_NUMBER = "1234";
  public static final String TEST_PAYPAL_ORDER_ID = "2D586271301114849";
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
          .paymentDate(new Date())
          .build();

  public static final PayPalPayment TEST_PAYPAL_PAYMENT =
      PayPalPayment.payPalPaymentBuilder()
          .investment(InvestmentUtils.mockOwnerApprovedInvestment())
          .currency(PlatformSettingsUtils.TEST_PLATFORM_CURRENCY.getCode())
          .amount(BigDecimal.valueOf(100L))
          .payPalOrderId(TEST_PAYPAL_ORDER_ID)
          .build();

  public static Order getTestOrder() {
    AmountWithBreakdown testAmountWithBreakdown = new AmountWithBreakdown();
    testAmountWithBreakdown.value("100");
    PurchaseUnit testPurchaseUnit = new PurchaseUnit();
    testPurchaseUnit.amountWithBreakdown(testAmountWithBreakdown);
    List<PurchaseUnit> testPurchaseUnitList = new ArrayList<>();
    testPurchaseUnitList.add(0, testPurchaseUnit);
    Order testOrder = new Order();
    testOrder.purchaseUnits(testPurchaseUnitList);
    testOrder.createTime(Instant.now().toString());
    return testOrder;
  }

  public static Order getTestOrderWithWrongAmount() {
    AmountWithBreakdown testAmountWithBreakdown = new AmountWithBreakdown();
    testAmountWithBreakdown.value("90");
    PurchaseUnit testPurchaseUnit = new PurchaseUnit();
    testPurchaseUnit.amountWithBreakdown(testAmountWithBreakdown);
    List<PurchaseUnit> testPurchaseUnitList = new ArrayList<>();
    testPurchaseUnitList.add(0, testPurchaseUnit);
    Order testOrder = new Order();
    testOrder.purchaseUnits(testPurchaseUnitList);
    testOrder.createTime(Instant.now().toString());
    return testOrder;
  }
}
