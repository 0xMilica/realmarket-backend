package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.CountryRepository;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.InvestmentStateService;
import io.realmarket.propeler.service.PaymentService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.util.PersonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.*;
import static io.realmarket.propeler.util.CampaignUtils.*;
import static io.realmarket.propeler.util.InvestmentUtils.*;
import static io.realmarket.propeler.util.PersonUtils.TEST_PERSON;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(InvestmentServiceImpl.class)
public class InvestmentServiceImplTest {

  @InjectMocks private InvestmentServiceImpl investmentService;

  @Mock private InvestmentRepository investmentRepository;

  @Mock private CampaignService campaignService;

  @Mock private InvestmentStateService investmentStateService;

  @Mock private PaymentService paymentService;

  @Mock private PersonService personService;

  @Mock private ModelMapperBlankString modelMapperBlankString;

  @Mock private BlockchainCommunicationService blockchainCommunicationService;

  @Mock private CountryRepository countryRepository;

  @Before
  public void createAuthContext() {
    mockRequestAndContext();
    ReflectionTestUtils.setField(investmentService, "weekInMillis", 604800000);
  }

  @Test
  public void invest_Should_Create_Investment() {
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);
    when(investmentStateService.getInvestmentState(InvestmentStateName.INITIAL))
        .thenReturn(TEST_INVESTMENT_INITIAL_STATE);
    when(investmentRepository.save(any(Investment.class))).thenReturn(TEST_INVESTMENT_INITIAL);

    Investment investment =
        investmentService.invest(BigDecimal.valueOf(100), TEST_URL_FRIENDLY_NAME);

    assertEquals(InvestmentStateName.INITIAL, investment.getInvestmentState().getName());
  }

  @Test(expected = BadRequestException.class)
  public void invest_Should_Throw_Exception_When_Campaign_Not_Active() {
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_CAMPAIGN);
    doThrow(BadRequestException.class)
        .when(campaignService)
        .throwIfNotActive(TEST_INVESTABLE_CAMPAIGN);

    investmentService.invest(BigDecimal.valueOf(100), TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = BadRequestException.class)
  public void invest_Should_Throw_Exception_When_Negative_Value() {
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);

    investmentService.invest(BigDecimal.valueOf(-100), TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = BadRequestException.class)
  public void invest_Should_Throw_Exception_When_Investment_Lesser_Than_Campaign_Minimum() {
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);

    investmentService.invest(BigDecimal.ZERO, TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = BadRequestException.class)
  public void invest_Should_Throw_Exception_When_Investment_Greater_Than_Campaign_Max_Investment() {
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);

    investmentService.invest(BigDecimal.valueOf(1000), TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void offPlatformInvest_Should_Create_Investment() {
    mockRequestAndContextAdmin();
    Person testPerson = TEST_PERSON.toBuilder().build();

    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);
    doNothing().when(modelMapperBlankString).map(TEST_OFFPLATFORM_INVESTMENT, testPerson);
    when(investmentStateService.getInvestmentState(InvestmentStateName.INITIAL))
        .thenReturn(TEST_INVESTMENT_INITIAL_STATE);
    when(countryRepository.findByCode(TEST_COUNTRY_CODE)).thenReturn(Optional.of(TEST_COUNTRY));
    when(investmentRepository.save(any(Investment.class))).thenReturn(TEST_INVESTMENT_INITIAL);

    Investment investment =
        investmentService.offPlatformInvest(TEST_OFFPLATFORM_INVESTMENT, TEST_URL_FRIENDLY_NAME);

    assertEquals(InvestmentStateName.INITIAL, investment.getInvestmentState().getName());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void offPlatformInvest_Should_Throw_Exception_When_Not_Admin() {
    investmentService.offPlatformInvest(
        TEST_OFFPLATFORM_INVESTMENT_AMOUNT_GREATER_THAN_MAXIMUM, TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = BadRequestException.class)
  public void offPlatformInvest_Should_Throw_Exception_When_Campaign_Not_Active() {
    mockRequestAndContextAdmin();
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_CAMPAIGN);
    doThrow(BadRequestException.class)
        .when(campaignService)
        .throwIfNotActive(TEST_INVESTABLE_CAMPAIGN);

    investmentService.offPlatformInvest(TEST_OFFPLATFORM_INVESTMENT, TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = BadRequestException.class)
  public void offPlatformInvest_Should_Throw_Exception_When_Negative_Value() {
    mockRequestAndContextAdmin();
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);

    investmentService.offPlatformInvest(
        TEST_OFFPLATFORM_INVESTMENT_NEGATIVE_AMOUNT, TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = BadRequestException.class)
  public void
      offPlatformInvest_Should_Throw_Exception_When_Investment_Lesser_Than_Campaign_Minimum() {
    mockRequestAndContextAdmin();
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);

    investmentService.offPlatformInvest(
        TEST_OFFPLATFORM_INVESTMENT_AMOUNT_LESSER_THAN_MINIMUM, TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = BadRequestException.class)
  public void
      offPlatformInvest_Should_Throw_Exception_When_Investment_Greater_Than_Campaign_Max_Investment() {
    mockRequestAndContextAdmin();
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);

    investmentService.offPlatformInvest(
        TEST_OFFPLATFORM_INVESTMENT_AMOUNT_GREATER_THAN_MAXIMUM, TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void revokeInvestment_Should_Revoke_Investment() {
    Person person = PersonUtils.TEST_PERSON;
    Investment actualInvestment = TEST_INVESTMENT_PAID_REVOCABLE.toBuilder().build();
    actualInvestment.setPerson(person);

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(actualInvestment);
    doNothing().when(paymentService).withdrawFunds(person, actualInvestment.getInvestedAmount());
    when(investmentStateService.getInvestmentState(InvestmentStateName.REVOKED))
        .thenReturn(TEST_INVESTMENT_REVOKED_STATE);
    when(investmentRepository.save(actualInvestment)).thenReturn(actualInvestment);

    investmentService.revokeInvestment(INVESTMENT_ID);
    assertEquals(TEST_INVESTMENT_REVOKED_STATE, actualInvestment.getInvestmentState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void revokeInvestment_Should_Throw_Exception_When_Not_Campaign_Investor() {
    mockRequestAndContextEntrepreneur();
    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(TEST_INVESTMENT_INITIAL);

    investmentService.revokeInvestment(INVESTMENT_ID);
  }

  @Test(expected = BadRequestException.class)
  public void revokeInvestment_Should_Throw_Exception_When_Investment_Not_Revocable() {
    Person person = AuthenticationUtil.getAuthentication().getAuth().getPerson();
    Investment actualInvestment = TEST_INVESTMENT_PAID_NOT_REVOCABLE.toBuilder().build();
    actualInvestment.setPerson(person);

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(actualInvestment);

    investmentService.revokeInvestment(INVESTMENT_ID);
  }

  @Test
  public void ownerApproveInvestment_Should_Approve_Investment() {
    Investment investment = TEST_INVESTMENT_INITIAL.toBuilder().build();

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(investment);
    doNothing().when(campaignService).throwIfNotOwner(TEST_INVESTABLE_CAMPAIGN);
    when(investmentStateService.getInvestmentState(InvestmentStateName.INITIAL))
        .thenReturn(TEST_INVESTMENT_INITIAL_STATE);
    when(investmentStateService.getInvestmentState(InvestmentStateName.OWNER_APPROVED))
        .thenReturn(TEST_INVESTMENT_OWNER_APPROVED_STATE);
    when(investmentRepository.save(investment)).thenReturn(investment);

    investmentService.ownerApproveInvestment(INVESTMENT_ID);
    assertEquals(TEST_INVESTMENT_OWNER_APPROVED_STATE, investment.getInvestmentState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void ownerApproveInvestment_Should_Throw_Exception_When_Not_Campaign_Owner() {
    Investment investment = TEST_INVESTMENT_INITIAL.toBuilder().build();

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(investment);
    doThrow(ForbiddenOperationException.class)
        .when(campaignService)
        .throwIfNotOwner(TEST_INVESTABLE_CAMPAIGN);

    investmentService.ownerApproveInvestment(INVESTMENT_ID);
  }

  @Test
  public void ownerRejectInvestment_Should_Reject_Investment() {
    Investment investment = TEST_INVESTMENT_INITIAL.toBuilder().build();

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(investment);
    doNothing().when(campaignService).throwIfNotOwner(TEST_INVESTABLE_CAMPAIGN);
    when(investmentStateService.getInvestmentState(InvestmentStateName.INITIAL))
        .thenReturn(TEST_INVESTMENT_INITIAL_STATE);
    when(investmentStateService.getInvestmentState(InvestmentStateName.OWNER_REJECTED))
        .thenReturn(TEST_INVESTMENT_OWNER_REJECTED_STATE);
    when(investmentRepository.save(investment)).thenReturn(investment);

    investmentService.ownerRejectInvestment(INVESTMENT_ID);
    assertEquals(TEST_INVESTMENT_OWNER_REJECTED_STATE, investment.getInvestmentState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void ownerRejectInvestment_Should_Throw_Exception_When_Not_Campaign_Owner() {
    Investment investment = TEST_INVESTMENT_INITIAL.toBuilder().build();

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(investment);
    doThrow(ForbiddenOperationException.class)
        .when(campaignService)
        .throwIfNotOwner(TEST_INVESTABLE_CAMPAIGN);

    investmentService.ownerRejectInvestment(INVESTMENT_ID);
  }

  @Test
  public void auditApproveInvestment_Should_Approve_Investment() {
    mockRequestAndContextAdmin();
    Investment investment = TEST_INVESTMENT_PAID_NOT_REVOCABLE.toBuilder().build();

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(investment);
    when(investmentStateService.getInvestmentState(InvestmentStateName.PAID))
        .thenReturn(TEST_INVESTMENT_PAID_STATE);
    when(investmentStateService.getInvestmentState(InvestmentStateName.AUDIT_APPROVED))
        .thenReturn(TEST_INVESTMENT_AUDIT_APPROVED_STATE);
    when(investmentRepository.save(investment)).thenReturn(investment);

    investmentService.auditorApproveInvestment(INVESTMENT_ID);
    assertEquals(TEST_INVESTMENT_AUDIT_APPROVED_STATE, investment.getInvestmentState());
  }

  @Test(expected = BadRequestException.class)
  public void auditApproveInvestment_Should_Throw_Exception_When_Investment_Revocable() {
    mockRequestAndContextAdmin();
    Investment investment = TEST_INVESTMENT_PAID_REVOCABLE.toBuilder().build();

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(investment);
    when(investmentStateService.getInvestmentState(InvestmentStateName.PAID))
        .thenReturn(TEST_INVESTMENT_PAID_STATE);

    investmentService.auditorApproveInvestment(INVESTMENT_ID);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void auditApproveInvestment_Should_Throw_Exception_When_Not_Admin() {
    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(TEST_INVESTMENT_PAID_NOT_REVOCABLE);

    investmentService.auditorApproveInvestment(INVESTMENT_ID);
  }

  @Test(expected = BadRequestException.class)
  public void auditApproveInvestment_Should_Throw_Exception_When_Not_Paid() {
    mockRequestAndContextAdmin();
    Investment investment = TEST_INVESTMENT_NOT_PAID_REVOCABLE.toBuilder().build();

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(investment);
    when(investmentStateService.getInvestmentState(InvestmentStateName.PAID))
        .thenReturn(TEST_INVESTMENT_PAID_STATE);

    investmentService.auditorApproveInvestment(INVESTMENT_ID);
  }

  @Test
  public void auditRejectInvestment_Should_Reject_Investment() {
    mockRequestAndContextAdmin();
    Investment investment = TEST_INVESTMENT_PAID_NOT_REVOCABLE.toBuilder().build();

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(investment);
    doNothing()
        .when(paymentService)
        .withdrawFunds(PersonUtils.TEST_PERSON, investment.getInvestedAmount());
    when(investmentStateService.getInvestmentState(InvestmentStateName.AUDIT_REJECTED))
        .thenReturn(TEST_INVESTMENT_AUDIT_REJECTED_STATE);
    when(investmentRepository.save(investment)).thenReturn(investment);

    investmentService.auditorRejectInvestment(INVESTMENT_ID);
    assertEquals(TEST_INVESTMENT_AUDIT_REJECTED_STATE, investment.getInvestmentState());
  }

  @Test(expected = BadRequestException.class)
  public void auditRejectInvestment_Should_Throw_Exception_When_Investment_Revocable() {
    mockRequestAndContextAdmin();
    Investment investment = TEST_INVESTMENT_PAID_REVOCABLE.toBuilder().build();

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(investment);

    investmentService.auditorRejectInvestment(INVESTMENT_ID);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void auditRejectInvestment_Should_Throw_Exception_When_Not_Admin() {

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(TEST_INVESTMENT_PAID_NOT_REVOCABLE);
    doThrow(ForbiddenOperationException.class)
        .when(campaignService)
        .throwIfNotOwner(TEST_INVESTABLE_CAMPAIGN);

    investmentService.auditorRejectInvestment(INVESTMENT_ID);
  }
}
