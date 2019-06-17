package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;

import java.math.BigDecimal;

public interface PaymentService {

  void reserveFunds(Auth auth, BigDecimal amountOfMoney);

  boolean proceedToPayment(Auth auth, BigDecimal amountOfMoney);

  void withdrawFunds(Auth auth, BigDecimal amountOfMoney);
}
