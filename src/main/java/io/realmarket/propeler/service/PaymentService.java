package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.realmarket.propeler.model.Payment;
import io.realmarket.propeler.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface PaymentService {

  Page<PaymentResponseDto> getPayments(Pageable pageable, String filter);

  Payment confirmBankTransferPayment(
      Long investmentId, PaymentConfirmationDto paymentConfirmationDto);

  void reserveFunds(Person person, BigDecimal amountOfMoney);

  boolean proceedToPayment(Person person, BigDecimal amountOfMoney);

  void withdrawFunds(Person person, BigDecimal amountOfMoney);
}
