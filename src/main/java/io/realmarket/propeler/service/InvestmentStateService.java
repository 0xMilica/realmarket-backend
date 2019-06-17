package io.realmarket.propeler.service;

import io.realmarket.propeler.model.InvestmentState;
import io.realmarket.propeler.model.enums.InvestmentStateName;

public interface InvestmentStateService {

  InvestmentState getInvestmentState(String name);

  InvestmentState getInvestmentState(InvestmentStateName investmentStateName);
}
