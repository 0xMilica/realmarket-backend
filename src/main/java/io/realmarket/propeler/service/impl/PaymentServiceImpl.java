package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.service.PaymentService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {

  private final InvestmentRepository investmentRepository;

  @Autowired
  public PaymentServiceImpl(InvestmentRepository investmentRepository) {
    this.investmentRepository = investmentRepository;
  }

  @Override
  public Page<PaymentResponseDto> getPayments(Pageable pageable, String filter) {
    throwIfNotAllowedFilter(filter);
    InvestmentStateName state =
        (filter == null) ? null : InvestmentStateName.valueOf(filter.toUpperCase());
    return investmentRepository
        .findAllPaymentInvestment(state, pageable)
        .map(PaymentResponseDto::new);
  }

  private void throwIfNotAllowedFilter(String filter) {
    if (filter == null) {
      return;
    }
    if (!(filter.equalsIgnoreCase("owner_approved")
        || filter.equalsIgnoreCase("paid")
        || filter.equalsIgnoreCase("expired"))) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }
  }

  @Override
  public void reserveFunds(Person person, BigDecimal amountOfMoney) {}

  @Override
  public boolean proceedToPayment(Person person, BigDecimal amountOfMoney) {
    return true;
  }

  @Override
  public void withdrawFunds(Person person, BigDecimal amountOfMoney) {}
}
