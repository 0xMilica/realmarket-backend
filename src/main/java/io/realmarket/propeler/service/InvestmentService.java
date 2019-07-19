package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.InvestmentWithPersonResponseDto;
import io.realmarket.propeler.api.dto.OffPlatformInvestmentRequestDto;
import io.realmarket.propeler.api.dto.PortfolioCampaignResponseDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface InvestmentService {

  Investment invest(BigDecimal amountOfMoney, String campaignUrlFriendlyName);

  Investment offPlatformInvest(
      OffPlatformInvestmentRequestDto offPlatformInvestmentRequestDto,
      String campaignUrlFriendlyName);

  void ownerApproveInvestment(Long investmentId);

  void ownerRejectInvestment(Long investmentId);

  void revokeInvestment(Long investmentId);

  void auditorApproveInvestment(Long investmentId);

  void auditorRejectInvestment(Long investmentId);

  Page<PortfolioCampaignResponseDto> getPortfolio(Pageable pageable, String filter);

  List<Investment> findAllByCampaign(Campaign campaign);

  List<InvestmentWithPersonResponseDto> findAllByCampaignWithInvestors(Campaign campaign);

  List<Investment> findAllByCampaignAndPerson(Campaign campaign, Person person);
}
