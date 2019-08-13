package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.realmarket.propeler.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface PaymentService {

  Page<PaymentResponseDto> getPayments(Pageable pageable, String filter);

  void reserveFunds(Person person, BigDecimal amountOfMoney);

  boolean proceedToPayment(Person person, BigDecimal amountOfMoney);

  void withdrawFunds(Person person, BigDecimal amountOfMoney);
}
