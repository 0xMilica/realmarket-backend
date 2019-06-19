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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
public class InvestmentServiceImpl implements InvestmentService {

  @Value("${app.investment.weekInMillis}")
  private long weekInMillis;

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
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignUrlFriendlyName);
    campaignService.throwIfNotActive(campaign);
    throwIfAmountNotValid(campaign, amountOfMoney);

    CampaignInvestment campaignInvestment =
        CampaignInvestment.builder()
            .auth(AuthenticationUtil.getAuthentication().getAuth())
            .campaign(campaign)
            .investedAmount(amountOfMoney)
            .investmentState(investmentStateService.getInvestmentState(InvestmentStateName.INITIAL))
            .build();

    return campaignInvestmentRepository.save(campaignInvestment);
  }

  @Transactional
  @Override
  public void revokeInvestment(Long campaignInvestmentId) {

    CampaignInvestment campaignInvestment =
        campaignInvestmentRepository.getOne(campaignInvestmentId);
    throwIfNoAccess(campaignInvestment);
    throwIfNotRevocable(campaignInvestment);

    paymentService.withdrawFunds(
        campaignInvestment.getAuth(), campaignInvestment.getInvestedAmount());

    campaignInvestment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.REVOKED));
    campaignInvestmentRepository.save(campaignInvestment);
  }

  @Transactional
  @Override
  public void approveInvestment(Long campaignInvestmentId) {

    CampaignInvestment campaignInvestment =
        campaignInvestmentRepository.getOne(campaignInvestmentId);
    throwIfRevocable(campaignInvestment);

    Campaign campaign = campaignInvestment.getCampaign();
    campaignService.throwIfNoAccess(campaign);

    throwIfNotPaid(campaignInvestment);
    campaignService.increaseCollectedAmount(campaign, campaignInvestment.getInvestedAmount());

    campaignInvestment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.APPROVED));
    campaignInvestmentRepository.save(campaignInvestment);
  }

  @Transactional
  @Override
  public void rejectInvestment(Long campaignInvestmentId) {
    CampaignInvestment campaignInvestment =
        campaignInvestmentRepository.getOne(campaignInvestmentId);
    throwIfRevocable(campaignInvestment);

    Campaign campaign = campaignInvestment.getCampaign();
    campaignService.throwIfNoAccess(campaign);

    Auth auth = campaignInvestment.getAuth();
    BigDecimal amountOfMoney = campaignInvestment.getInvestedAmount();

    paymentService.withdrawFunds(auth, amountOfMoney);

    campaignInvestment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.REJECTED));
    campaignInvestmentRepository.save(campaignInvestment);
  }

  private void throwIfNotPaid(CampaignInvestment campaignInvestment) {
    if (!isPaid(campaignInvestment)) {
      throw new BadRequestException(PAYMENT_NOT_PROCESSED);
    }
  }

  private boolean isPaid(CampaignInvestment campaignInvestment) {
    return campaignInvestment
        .getInvestmentState()
        .equals(investmentStateService.getInvestmentState(InvestmentStateName.PAID));
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

  private boolean isRevocable(CampaignInvestment campaignInvestment) {
    if (campaignInvestment.getPaymentDate() == null) {
      return true;
    } else {
      return campaignInvestment.getPaymentDate().plusMillis(weekInMillis).isAfter(Instant.now());
    }
  }

  private void throwIfNotRevocable(CampaignInvestment campaignInvestment) {
    if (!isRevocable(campaignInvestment)) {
      throw new BadRequestException(INVESTMENT_CAN_NOT_BE_REVOKED);
    }
  }

  private void throwIfRevocable(CampaignInvestment campaignInvestment) {
    if (isRevocable(campaignInvestment)) {
      throw new BadRequestException(INVESTMENT_CAN_BE_REVOKED);
    }
  }
}
