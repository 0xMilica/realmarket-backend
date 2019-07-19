package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.InvestmentStateService;
import io.realmarket.propeler.service.PaymentService;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.util.AuthUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContextAdmin;
import static io.realmarket.propeler.util.CampaignUtils.*;
import static io.realmarket.propeler.util.InvestmentUtils.*;
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

  @Mock private BlockchainCommunicationService blockchainCommunicationService;

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

    assertEquals(TEST_INVESTMENT_INITIAL_STATE, investment.getInvestmentState());
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
  public void invest_Should_Throw_Exception_When_Investment_Smaller_Than_Campaign_Minimum() {
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
  public void revokeInvestment_Should_Revoke_Investment() {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    Investment actualInvestment = TEST_INVESTMENT_PAID_REVOCABLE.toBuilder().build();
    actualInvestment.setAuth(auth);

    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(actualInvestment);
    doNothing().when(paymentService).withdrawFunds(auth, actualInvestment.getInvestedAmount());
    when(investmentStateService.getInvestmentState(InvestmentStateName.REVOKED))
        .thenReturn(TEST_INVESTMENT_REVOKED_STATE);
    when(investmentRepository.save(actualInvestment)).thenReturn(actualInvestment);

    investmentService.revokeInvestment(INVESTMENT_ID);
    assertEquals(TEST_INVESTMENT_REVOKED_STATE, actualInvestment.getInvestmentState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void revokeInvestment_Should_Throw_Exception_When_Not_Campaign_Investor() {
    when(investmentRepository.getOne(INVESTMENT_ID)).thenReturn(TEST_INVESTMENT_INITIAL);

    investmentService.revokeInvestment(INVESTMENT_ID);
  }

  @Test(expected = BadRequestException.class)
  public void revokeInvestment_Should_Throw_Exception_When_Investment_Not_Revocable() {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    Investment actualInvestment = TEST_INVESTMENT_PAID_NOT_REVOCABLE.toBuilder().build();
    actualInvestment.setAuth(auth);

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
        .withdrawFunds(AuthUtils.TEST_AUTH_INVESTOR, investment.getInvestedAmount());
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
