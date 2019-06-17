package io.realmarket.propeler.service;

import io.realmarket.propeler.model.CampaignInvestment;

import java.math.BigDecimal;

public interface InvestmentService {
  CampaignInvestment invest(BigDecimal amountOfMoney, String campaignUrlFriendlyName);

  void cancelInvestment(Long campaignInvestmentId);

  void approveInvestment(Long campaignInvestmentId);

  void declineInvestment(Long campaignInvestmentId);
}
