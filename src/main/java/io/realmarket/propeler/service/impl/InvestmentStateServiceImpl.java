package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.InvestmentState;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.InvestmentStateRepository;
import io.realmarket.propeler.service.InvestmentStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVESTMENT_STATE_NOT_FOUND;

@Service
public class InvestmentStateServiceImpl implements InvestmentStateService {

  private final InvestmentStateRepository investmentStateRepository;

  @Autowired
  public InvestmentStateServiceImpl(InvestmentStateRepository investmentStateRepository) {
    this.investmentStateRepository = investmentStateRepository;
  }

  @Override
  public InvestmentState getInvestmentState(String name) {
    return investmentStateRepository
        .findByName(InvestmentStateName.valueOf(name.toUpperCase()))
        .orElseThrow(() -> new EntityNotFoundException(INVESTMENT_STATE_NOT_FOUND));
  }

  @Override
  public InvestmentState getInvestmentState(InvestmentStateName investmentStateName) {
    return investmentStateRepository
        .findByName(investmentStateName)
        .orElseThrow(() -> new EntityNotFoundException(INVESTMENT_STATE_NOT_FOUND));
  }
}
