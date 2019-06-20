package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.PortfolioCampaignResponseDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignInvestment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface InvestmentService {

  List<CampaignInvestment> findAllByCampaignAndAuth(Campaign campaign, Auth auth);

  CampaignInvestment invest(BigDecimal amountOfMoney, String campaignUrlFriendlyName);

  void revokeInvestment(Long campaignInvestmentId);

  void approveInvestment(Long campaignInvestmentId);

  void rejectInvestment(Long campaignInvestmentId);

  Page<PortfolioCampaignResponseDto> getPortfolio(Pageable pageable);
}
