package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.investment.ChangeStateDto;
import io.realmarket.propeler.service.blockchain.dto.investment.InvestmentDto;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
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
  private final ModelMapperBlankString modelMapperBlankString;
  private final PersonService personService;
  private final BlockchainCommunicationService blockchainCommunicationService;

  @Value("${app.investment.weekInMillis}")
  private long weekInMillis;

  @Autowired
  public InvestmentServiceImpl(
      CampaignService campaignService,
      InvestmentRepository investmentRepository,
      PaymentService paymentService,
      InvestmentStateService investmentStateService,
      ModelMapperBlankString modelMapperBlankString,
      PersonService personService,
      BlockchainCommunicationService blockchainCommunicationService) {
    this.campaignService = campaignService;
    this.investmentRepository = investmentRepository;
    this.paymentService = paymentService;
    this.investmentStateService = investmentStateService;
    this.modelMapperBlankString = modelMapperBlankString;
    this.personService = personService;
    this.blockchainCommunicationService = blockchainCommunicationService;
  }

  @Transactional
  @Override
  public Investment invest(BigDecimal amountOfMoney, String campaignUrlFriendlyName) {
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignUrlFriendlyName);
    campaignService.throwIfNotActive(campaign);
    throwIfAmountNotValid(campaign, amountOfMoney);

    Investment investment =
        Investment.builder()
            .person(AuthenticationUtil.getAuthentication().getAuth().getPerson())
            .campaign(campaign)
            .investedAmount(amountOfMoney)
            .investmentState(investmentStateService.getInvestmentState(InvestmentStateName.INITIAL))
            .build();

    investment = investmentRepository.save(investment);

    blockchainCommunicationService.invoke(
        BlockchainMethod.INVESTMENT_INTENT,
        new InvestmentDto(investment),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return investment;
  }

  @Transactional
  @Override
  public Investment offPlatformInvest(
      OffPlatformInvestmentRequestDto offPlatformInvestmentRequestDto,
      String campaignUrlFriendlyName) {
    throwIfNotAdmin();
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignUrlFriendlyName);
    campaignService.throwIfNotActive(campaign);
    throwIfAmountNotValid(campaign, offPlatformInvestmentRequestDto.getInvestedAmount());

    Person person = new Person();
    modelMapperBlankString.map(offPlatformInvestmentRequestDto, person);
    personService.save(person);

    Investment investment =
        Investment.builder()
            .person(person)
            .campaign(campaign)
            .investedAmount(offPlatformInvestmentRequestDto.getInvestedAmount())
            .investmentState(investmentStateService.getInvestmentState(InvestmentStateName.INITIAL))
            .build();

    investment = investmentRepository.save(investment);

    blockchainCommunicationService.invoke(
        BlockchainMethod.INVESTMENT_INTENT,
        new InvestmentDto(investment, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return investment;
  }

  @Transactional
  @Override
  public void ownerApproveInvestment(Long investmentId) {
    Investment investment = investmentRepository.getOne(investmentId);

    Campaign campaign = investment.getCampaign();
    campaignService.throwIfNotOwner(campaign);

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.OWNER_APPROVED));
    investment = investmentRepository.save(investment);

    blockchainCommunicationService.invoke(
        BlockchainMethod.INVESTMENT_STATE_CHANGE,
        new ChangeStateDto(investment, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());
  }

  @Transactional
  @Override
  public void ownerRejectInvestment(Long investmentId) {
    Investment investment = investmentRepository.getOne(investmentId);

    Campaign campaign = investment.getCampaign();
    campaignService.throwIfNotOwner(campaign);

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.OWNER_REJECTED));
    investment = investmentRepository.save(investment);

    blockchainCommunicationService.invoke(
        BlockchainMethod.INVESTMENT_STATE_CHANGE,
        new ChangeStateDto(investment, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());
  }

  @Transactional
  @Override
  public void revokeInvestment(Long investmentId) {
    Investment investment = investmentRepository.getOne(investmentId);
    throwIfNoAccess(investment);
    throwIfNotRevocable(investment);

    paymentService.withdrawFunds(investment.getPerson(), investment.getInvestedAmount());

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.REVOKED));
    investment = investmentRepository.save(investment);

    blockchainCommunicationService.invoke(
        BlockchainMethod.INVESTMENT_STATE_CHANGE,
        new ChangeStateDto(investment, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());
  }

  @Transactional
  @Override
  public void auditorApproveInvestment(Long investmentId) {
    throwIfNotAdmin();
    Investment investment = investmentRepository.getOne(investmentId);
    throwIfRevocable(investment);

    Campaign campaign = investment.getCampaign();

    throwIfNotPaid(investment);
    campaignService.increaseCollectedAmount(campaign, investment.getInvestedAmount());

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.AUDIT_APPROVED));
    investment = investmentRepository.save(investment);

    blockchainCommunicationService.invoke(
        BlockchainMethod.INVESTMENT_STATE_CHANGE,
        new ChangeStateDto(investment, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());
  }

  @Transactional
  @Override
  public void auditorRejectInvestment(Long investmentId) {
    throwIfNotAdmin();
    Investment investment = investmentRepository.getOne(investmentId);
    throwIfRevocable(investment);

    BigDecimal amountOfMoney = investment.getInvestedAmount();

    paymentService.withdrawFunds(investment.getPerson(), amountOfMoney);

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.AUDIT_REJECTED));
    investment = investmentRepository.save(investment);

    blockchainCommunicationService.invoke(
        BlockchainMethod.INVESTMENT_STATE_CHANGE,
        new ChangeStateDto(investment, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());
  }

  @Override
  public Page<PortfolioCampaignResponseDto> getPortfolio(Pageable pageable, String filter) {
    Person person = AuthenticationUtil.getAuthentication().getAuth().getPerson();

    Page<Campaign> campaignPage;
    if (filter.equalsIgnoreCase("all")) {
      campaignPage = findInvestedCampaign(person, pageable);
    } else if (filter.equalsIgnoreCase("active") || filter.equalsIgnoreCase("post_campaign")) {
      campaignPage =
          findInvestedCampaignByState(
              person, CampaignStateName.valueOf(filter.toUpperCase()), pageable);
    } else {
      throw new BadRequestException(INVALID_REQUEST);
    }

    List<PortfolioCampaignResponseDto> portfolio = new ArrayList<>();
    campaignPage
        .getContent()
        .forEach(
            campaign -> {
              PortfolioCampaignResponseDto portfolioCampaign =
                  convertCampaignToPortfolioCampaign(campaign, person);
              portfolio.add(portfolioCampaign);
            });

    return new PageImpl<>(portfolio, pageable, campaignPage.getTotalElements());
  }

  private PortfolioCampaignResponseDto convertCampaignToPortfolioCampaign(
      Campaign campaign, Person person) {
    PortfolioCampaignResponseDto portfolioCampaign = new PortfolioCampaignResponseDto();

    portfolioCampaign.setCampaign(new CampaignResponseDto(campaign));

    List<Investment> investments = findAllByCampaignAndPerson(campaign, person);
    List<InvestmentResponseDto> investmentsDto =
        investments.stream().map(InvestmentResponseDto::new).collect(Collectors.toList());
    portfolioCampaign.setInvestments(investmentsDto);

    portfolioCampaign.setTotal(new TotalInvestmentsResponseDto(investments));

    return portfolioCampaign;
  }

  @Override
  public List<Investment> findAllByCampaignAndPerson(Campaign campaign, Person person) {
    return investmentRepository.findAllByCampaignAndPerson(campaign, person);
  }

  @Override
  public List<Investment> findAllByCampaign(Campaign campaign) {
    return investmentRepository.findAllByCampaign(campaign);
  }

  @Override
  public List<InvestmentWithPersonResponseDto> findAllByCampaignWithInvestors(Campaign campaign) {
    return findAllByCampaign(campaign).stream()
        .map(i -> new InvestmentWithPersonResponseDto(i, new PersonDto(i.getPerson())))
        .collect(Collectors.toList());
  }

  private Page<Campaign> findInvestedCampaign(Person person, Pageable pageable) {
    return investmentRepository.findInvestedCampaign(person, pageable);
  }

  private Page<Campaign> findInvestedCampaignByState(
      Person person, CampaignStateName state, Pageable pageable) {
    return investmentRepository.findInvestedCampaignByState(person, state, pageable);
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
        .getPerson()
        .getId()
        .equals(AuthenticationUtil.getAuthentication().getAuth().getPerson().getId());
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
