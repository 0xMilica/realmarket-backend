package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
  @Override
  public void reserveFunds(Auth auth, BigDecimal amountOfMoney) {}

  @Override
  public boolean proceedToPayment(Auth auth, BigDecimal amountOfMoney) {
    return true;
  }

  @Override
  public void withdrawFunds(Auth auth, BigDecimal amountOfMoney) {}
}
