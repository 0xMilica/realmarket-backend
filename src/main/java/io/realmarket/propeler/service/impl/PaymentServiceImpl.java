package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
  @Override
  public void reserveFunds(Person person, BigDecimal amountOfMoney) {}

  @Override
  public boolean proceedToPayment(Person person, BigDecimal amountOfMoney) {
    return true;
  }

  @Override
  public void withdrawFunds(Person person, BigDecimal amountOfMoney) {}
}
