package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignInvestment;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.CampaignInvestmentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.InvestmentService;
import io.realmarket.propeler.service.InvestmentStateService;
import io.realmarket.propeler.service.PaymentService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
public class InvestmentServiceImpl implements InvestmentService {

  private static final long WEEK = 604800000L;
  private final CampaignService campaignService;
  private final CampaignInvestmentRepository campaignInvestmentRepository;
  private final PaymentService paymentService;
  private final InvestmentStateService investmentStateService;

  @Autowired
  public InvestmentServiceImpl(
      CampaignService campaignService,
      CampaignInvestmentRepository campaignInvestmentRepository,
      PaymentService paymentService,
      InvestmentStateService investmentStateService) {
    this.campaignService = campaignService;
    this.campaignInvestmentRepository = campaignInvestmentRepository;
    this.paymentService = paymentService;
    this.investmentStateService = investmentStateService;
  }

  @Transactional
  @Override
  public CampaignInvestment invest(BigDecimal amountOfMoney, String campaignUrlFriendlyName) {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignUrlFriendlyName);
    throwIfAmountNotValid(campaign, amountOfMoney);

    CampaignInvestment campaignInvestment =
        CampaignInvestment.builder()
            .auth(auth)
            .campaign(campaign)
            .investedAmount(amountOfMoney)
            .investmentState(investmentStateService.getInvestmentState(InvestmentStateName.PENDING))
            .build();

    paymentService.reserveFunds(auth, amountOfMoney);
    return campaignInvestmentRepository.save(campaignInvestment);
  }

  @Transactional
  @Override
  public void cancelInvestment(Long campaignInvestmentId) {

    CampaignInvestment campaignInvestment =
        campaignInvestmentRepository.getOne(campaignInvestmentId);
    throwIfNoAccess(campaignInvestment);

    paymentService.withdrawFunds(
        campaignInvestment.getAuth(), campaignInvestment.getInvestedAmount());

    campaignInvestment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.CANCELLED));
    campaignInvestmentRepository.save(campaignInvestment);
  }

  @Transactional
  @Override
  public void approveInvestment(Long campaignInvestmentId) {

    CampaignInvestment campaignInvestment =
        campaignInvestmentRepository.getOne(campaignInvestmentId);
    Campaign campaign = campaignInvestment.getCampaign();
    campaignService.throwIfNoAccess(campaign);

    increaseCollectedAmount(campaignInvestment);

    campaignInvestment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.APPROVED));
    campaignInvestmentRepository.save(campaignInvestment);
  }

  @Transactional
  @Override
  public void declineInvestment(Long campaignInvestmentId) {
    CampaignInvestment campaignInvestment =
        campaignInvestmentRepository.getOne(campaignInvestmentId);
    Campaign campaign = campaignInvestment.getCampaign();
    campaignService.throwIfNoAccess(campaign);

    Auth auth = campaignInvestment.getAuth();
    BigDecimal amountOfMoney = campaignInvestment.getInvestedAmount();

    paymentService.withdrawFunds(auth, amountOfMoney);

    campaignInvestment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.DECLINED));
    campaignInvestmentRepository.save(campaignInvestment);
  }

  private void increaseCollectedAmount(CampaignInvestment campaignInvestment) {
    if (campaignInvestment.getCreationDate().plusMillis(WEEK).isAfter(Instant.now())
        && !campaignInvestment
            .getInvestmentState()
            .equals(investmentStateService.getInvestmentState(InvestmentStateName.CANCELLED))) {
      Campaign campaign = campaignInvestment.getCampaign();
      campaignService.increaseCollectedAmount(campaign, campaignInvestment.getInvestedAmount());
    }
  }

  private void throwIfAmountNotValid(Campaign campaign, BigDecimal amountOfMoney) {
    if (amountOfMoney.compareTo(BigDecimal.ZERO) < 0) {
      throw new BadRequestException(NEGATIVE_VALUE_EXCEPTION);
    }
    BigDecimal maxInvest =
        BigDecimal.valueOf(campaign.getFundingGoals())
            .multiply(
                campaign
                    .getMaxEquityOffered()
                    .divide(campaign.getMinEquityOffered(), MathContext.DECIMAL128))
            .subtract(campaign.getCollectedAmount());
    if (amountOfMoney.compareTo(campaign.getMinInvestment()) < 0) {
      throw new BadRequestException(INVESTMENT_MUST_BE_GREATER_THAN_CAMPAIGN_MIN_INVESTMENT);
    } else if (amountOfMoney.compareTo(maxInvest) > 0) {
      throw new BadRequestException(INVESTMENT_CAN_NOT_BE_GREATER_THAN_MAX_INVESTMENT);
    }
  }

  private boolean isCampaignInvestor(CampaignInvestment campaignInvestment) {
    return campaignInvestment
        .getAuth()
        .getId()
        .equals(AuthenticationUtil.getAuthentication().getAuth().getId());
  }

  private void throwIfNoAccess(CampaignInvestment campaignInvestment) {
    if (!isCampaignInvestor(campaignInvestment)) {
      throw new ForbiddenOperationException(NOT_CAMPAIGN_INVESTOR);
    }
  }
}
