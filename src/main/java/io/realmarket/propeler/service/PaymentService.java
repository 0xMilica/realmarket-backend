package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.realmarket.propeler.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

  List<String> getPaymentMethods(Long investmentId);

  BankTransferPayment getBankTransferPayment(Long investmentId);

  BankTransferPayment createBankTransferPayment(Investment investment);

  String getProformaInvoiceUrl(Long investmentId);

  CardPayment getCardPayment(Long investmentId);

  Page<PaymentResponseDto> getPayments(Pageable pageable, String filter);

  Payment confirmBankTransferPayment(
      Long investmentId, PaymentConfirmationDto paymentConfirmationDto);

  void reserveFunds(Person person, BigDecimal amountOfMoney);

  boolean proceedToPayment(Person person, BigDecimal amountOfMoney);

  void withdrawFunds(Person person, BigDecimal amountOfMoney);
}
