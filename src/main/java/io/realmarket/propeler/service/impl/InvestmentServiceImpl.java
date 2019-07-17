package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignResponseDto;
import io.realmarket.propeler.api.dto.InvestmentResponseDto;
import io.realmarket.propeler.api.dto.PortfolioCampaignResponseDto;
import io.realmarket.propeler.api.dto.TotalInvestmentsResponseDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.InvestmentService;
import io.realmarket.propeler.service.InvestmentStateService;
import io.realmarket.propeler.service.PaymentService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
public class InvestmentServiceImpl implements InvestmentService {

  private final CampaignService campaignService;
  private final InvestmentRepository investmentRepository;
  private final PaymentService paymentService;
  private final InvestmentStateService investmentStateService;

  @Value("${app.investment.weekInMillis}")
  private long weekInMillis;

  @Autowired
  public InvestmentServiceImpl(
      CampaignService campaignService,
      InvestmentRepository investmentRepository,
      PaymentService paymentService,
      InvestmentStateService investmentStateService) {
    this.campaignService = campaignService;
    this.investmentRepository = investmentRepository;
    this.paymentService = paymentService;
    this.investmentStateService = investmentStateService;
  }

  @Override
  public List<Investment> findAllByCampaignAndAuth(Campaign campaign, Auth auth) {
    return investmentRepository.findAllByCampaignAndAuth(campaign, auth);
  }

  public Page<Campaign> findInvestedCampaign(Auth auth, Pageable pageable) {
    return investmentRepository.findInvestedCampaign(auth, pageable);
  }

  public Page<Campaign> findInvestedCampaignByState(
      Auth auth, CampaignStateName state, Pageable pageable) {
    return investmentRepository.findInvestedCampaignByState(auth, state, pageable);
  }

  @Transactional
  @Override
  public Investment invest(BigDecimal amountOfMoney, String campaignUrlFriendlyName) {
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignUrlFriendlyName);
    campaignService.throwIfNotActive(campaign);
    throwIfAmountNotValid(campaign, amountOfMoney);

    Investment investment =
        Investment.builder()
            .auth(AuthenticationUtil.getAuthentication().getAuth())
            .campaign(campaign)
            .investedAmount(amountOfMoney)
            .investmentState(investmentStateService.getInvestmentState(InvestmentStateName.INITIAL))
            .build();

    return investmentRepository.save(investment);
  }

  @Transactional
  @Override
  public void ownerApproveInvestment(Long investmentId) {
    Investment investment = investmentRepository.getOne(investmentId);

    Campaign campaign = investment.getCampaign();
    campaignService.throwIfNotOwner(campaign);

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.OWNER_APPROVED));
    investmentRepository.save(investment);
  }

  @Transactional
  @Override
  public void ownerRejectInvestment(Long investmentId) {
    Investment investment = investmentRepository.getOne(investmentId);

    Campaign campaign = investment.getCampaign();
    campaignService.throwIfNotOwner(campaign);

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.OWNER_REJECTED));
    investmentRepository.save(investment);
  }

  @Transactional
  @Override
  public void revokeInvestment(Long investmentId) {
    Investment investment = investmentRepository.getOne(investmentId);
    throwIfNoAccess(investment);
    throwIfNotRevocable(investment);

    paymentService.withdrawFunds(investment.getAuth(), investment.getInvestedAmount());

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.REVOKED));
    investmentRepository.save(investment);
  }

  @Transactional
  @Override
  public void auditApproveInvestment(Long investmentId) {
    throwIfNotAdmin();
    Investment investment = investmentRepository.getOne(investmentId);
    throwIfRevocable(investment);

    Campaign campaign = investment.getCampaign();

    throwIfNotPaid(investment);
    campaignService.increaseCollectedAmount(campaign, investment.getInvestedAmount());

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.AUDIT_APPROVED));
    investmentRepository.save(investment);
  }

  @Transactional
  @Override
  public void auditRejectInvestment(Long investmentId) {
    throwIfNotAdmin();
    Investment investment = investmentRepository.getOne(investmentId);
    throwIfRevocable(investment);

    BigDecimal amountOfMoney = investment.getInvestedAmount();

    paymentService.withdrawFunds(investment.getAuth(), amountOfMoney);

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.AUDIT_REJECTED));
    investmentRepository.save(investment);
  }

  @Override
  public Page<PortfolioCampaignResponseDto> getPortfolio(Pageable pageable, String filter) {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();

    Page<Campaign> campaignPage;
    if (filter.equalsIgnoreCase("all")) {
      campaignPage = findInvestedCampaign(auth, pageable);
    } else if (filter.equalsIgnoreCase("active") || filter.equalsIgnoreCase("post_campaign")) {
      campaignPage =
          findInvestedCampaignByState(
              auth, CampaignStateName.valueOf(filter.toUpperCase()), pageable);
    } else {
      throw new BadRequestException(INVALID_REQUEST);
    }

    List<PortfolioCampaignResponseDto> portfolio = new ArrayList<>();
    campaignPage
        .getContent()
        .forEach(
            campaign -> {
              PortfolioCampaignResponseDto portfolioCampaign =
                  convertCampaignToPortfolioCampaign(campaign, auth);
              portfolio.add(portfolioCampaign);
            });

    return new PageImpl<>(portfolio, pageable, campaignPage.getTotalElements());
  }

  private PortfolioCampaignResponseDto convertCampaignToPortfolioCampaign(
      Campaign campaign, Auth auth) {
    PortfolioCampaignResponseDto portfolioCampaign = new PortfolioCampaignResponseDto();

    portfolioCampaign.setCampaign(new CampaignResponseDto(campaign));

    List<Investment> investments = findAllByCampaignAndAuth(campaign, auth);
    List<InvestmentResponseDto> investmentsDto =
        investments.stream().map(InvestmentResponseDto::new).collect(Collectors.toList());
    portfolioCampaign.setInvestments(investmentsDto);

    portfolioCampaign.setTotal(new TotalInvestmentsResponseDto(investments));

    return portfolioCampaign;
  }

  private void throwIfNotPaid(Investment investment) {
    if (!isPaid(investment)) {
      throw new BadRequestException(PAYMENT_NOT_PROCESSED);
    }
  }

  private boolean isPaid(Investment investment) {
    return investment
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

  private boolean isCampaignInvestor(Investment investment) {
    return investment
        .getAuth()
        .getId()
        .equals(AuthenticationUtil.getAuthentication().getAuth().getId());
  }

  private void throwIfNoAccess(Investment investment) {
    if (!isCampaignInvestor(investment)) {
      throw new ForbiddenOperationException(NOT_CAMPAIGN_INVESTOR);
    }
  }

  private void throwIfNotAdmin() {
    if (!isAdmin()) {
      throw new ForbiddenOperationException(FORBIDDEN_OPERATION_EXCEPTION);
    }
  }

  private boolean isAdmin() {
    return (AuthenticationUtil.getAuthentication()
        .getAuth()
        .getUserRole()
        .getName()
        .equals(UserRoleName.ROLE_ADMIN));
  }

  private void throwIfNotRevocable(Investment investment) {
    if (!isRevocable(investment)) {
      throw new BadRequestException(INVESTMENT_CAN_NOT_BE_REVOKED);
    }
  }

  private void throwIfRevocable(Investment investment) {
    if (isRevocable(investment)) {
      throw new BadRequestException(INVESTMENT_CAN_BE_REVOKED);
    }
  }

  private boolean isRevocable(Investment investment) {
    if (investment.getPaymentDate() == null) {
      return true;
    } else {
      return investment.getPaymentDate().plusMillis(weekInMillis).isAfter(Instant.now());
    }
  }
}
