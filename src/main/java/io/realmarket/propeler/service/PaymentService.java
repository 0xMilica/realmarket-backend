package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Person;

import java.math.BigDecimal;

public interface PaymentService {

  void reserveFunds(Person person, BigDecimal amountOfMoney);

  boolean proceedToPayment(Person person, BigDecimal amountOfMoney);

  void withdrawFunds(Person person, BigDecimal amountOfMoney);
}
