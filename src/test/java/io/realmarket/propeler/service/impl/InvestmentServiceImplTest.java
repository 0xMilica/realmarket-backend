package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.CampaignInvestment;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.CampaignInvestmentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.InvestmentStateService;
import io.realmarket.propeler.service.PaymentService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.util.CampaignUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;

import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.util.CampaignInvestmentUtils.*;
import static io.realmarket.propeler.util.CampaignUtils.TEST_INVESTABLE_CAMPAIGN;
import static io.realmarket.propeler.util.CampaignUtils.TEST_URL_FRIENDLY_NAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(InvestmentServiceImpl.class)
public class InvestmentServiceImplTest {

  @InjectMocks private InvestmentServiceImpl investmentService;

  @Mock private CampaignInvestmentRepository campaignInvestmentRepository;

  @Mock private CampaignService campaignService;

  @Mock private InvestmentStateService investmentStateService;

  @Mock private PaymentService paymentService;

  @Before
  public void createAuthContext() {
    mockRequestAndContext();
  }

  @Test
  public void invest_Should_Create_Investment() {
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);
    when(campaignInvestmentRepository.save(any(CampaignInvestment.class)))
        .thenReturn(TEST_CAMPAIGN_INVESTMENT_PENDING);
    when(investmentStateService.getInvestmentState(InvestmentStateName.PENDING))
        .thenReturn(TEST_INVESTMENT_PENDING_STATE);

    CampaignInvestment campaignInvestment =
        investmentService.invest(BigDecimal.valueOf(100), TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_CAMPAIGN_INVESTMENT_PENDING, campaignInvestment);
  }

  @Test(expected = BadRequestException.class)
  public void invest_Should_ThrowException_When_Negative_Value() {
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);

    CampaignInvestment campaignInvestment =
        investmentService.invest(BigDecimal.valueOf(-100), TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_CAMPAIGN_INVESTMENT_PENDING, campaignInvestment);
  }

  @Test(expected = BadRequestException.class)
  public void invest_Should_ThrowException_When_Investment_Smaller_Than_Campaign_Minimum() {
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);

    CampaignInvestment campaignInvestment =
        investmentService.invest(BigDecimal.ZERO, TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_CAMPAIGN_INVESTMENT_PENDING, campaignInvestment);
  }

  @Test(expected = BadRequestException.class)
  public void invest_Should_ThrowException_When_Investment_Greater_Than_Campaign_Max_Investment() {
    when(campaignService.getCampaignByUrlFriendlyName(TEST_URL_FRIENDLY_NAME))
        .thenReturn(TEST_INVESTABLE_CAMPAIGN);

    CampaignInvestment campaignInvestment =
        investmentService.invest(BigDecimal.valueOf(1000), TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_CAMPAIGN_INVESTMENT_PENDING, campaignInvestment);
  }

  @Test
  public void cancelInvestment_Should_Cancel_Investment() {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    CampaignInvestment campaignInvestment = TEST_CAMPAIGN_INVESTMENT_PENDING.toBuilder().build();
    campaignInvestment.setAuth(auth);
    when(campaignInvestmentRepository.getOne(1L)).thenReturn(campaignInvestment);
    doNothing()
        .when(paymentService)
        .withdrawFunds(auth, TEST_CAMPAIGN_INVESTMENT_PENDING.getInvestedAmount());
    when(investmentStateService.getInvestmentState(InvestmentStateName.CANCELLED))
        .thenReturn(TEST_INVESTMENT_CANCELLED_STATE);

    investmentService.cancelInvestment(1L);
    assertEquals(TEST_INVESTMENT_CANCELLED_STATE, campaignInvestment.getInvestmentState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void cancelInvestment_ShouldThrowException_When_Not_Campaign_Investor() {
    when(campaignInvestmentRepository.getOne(1L)).thenReturn(TEST_CAMPAIGN_INVESTMENT_PENDING);

    investmentService.cancelInvestment(1L);
  }

  @Test
  public void approveInvestment_Should_Approve_Investment() {
    when(campaignInvestmentRepository.getOne(1L)).thenReturn(TEST_CAMPAIGN_INVESTMENT_PENDING);
    doNothing().when(campaignService).throwIfNoAccess(CampaignUtils.TEST_CAMPAIGN);
    when(investmentStateService.getInvestmentState(InvestmentStateName.APPROVED))
        .thenReturn(TEST_INVESTMENT_APPROVED_STATE);

    investmentService.approveInvestment(1L);
    assertEquals(
        TEST_INVESTMENT_APPROVED_STATE, TEST_CAMPAIGN_INVESTMENT_PENDING.getInvestmentState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void approveInvestment_Should_ThrowException_When_Not_Campaign_Owner() {
    when(campaignInvestmentRepository.getOne(1L)).thenReturn(TEST_CAMPAIGN_INVESTMENT_PENDING);
    doThrow(ForbiddenOperationException.class)
        .when(campaignService)
        .throwIfNoAccess(CampaignUtils.TEST_CAMPAIGN);

    investmentService.approveInvestment(1L);
  }

  @Test
  public void declineInvestment_Should_Decline_Investment() {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();

    when(campaignInvestmentRepository.getOne(1L)).thenReturn(TEST_CAMPAIGN_INVESTMENT_PENDING);
    doNothing().when(campaignService).throwIfNoAccess(CampaignUtils.TEST_CAMPAIGN);
    doNothing()
        .when(paymentService)
        .withdrawFunds(auth, TEST_CAMPAIGN_INVESTMENT_PENDING.getInvestedAmount());
    when(investmentStateService.getInvestmentState(InvestmentStateName.DECLINED))
        .thenReturn(TEST_INVESTMENT_DECLINED_STATE);

    investmentService.declineInvestment(1L);
    assertEquals(
        TEST_INVESTMENT_DECLINED_STATE, TEST_CAMPAIGN_INVESTMENT_PENDING.getInvestmentState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void declineInvestment_Should_ThrowException_When_Not_Campaign_Owner() {

    when(campaignInvestmentRepository.getOne(1L)).thenReturn(TEST_CAMPAIGN_INVESTMENT_PENDING);
    doThrow(ForbiddenOperationException.class)
        .when(campaignService)
        .throwIfNoAccess(CampaignUtils.TEST_CAMPAIGN);

    investmentService.declineInvestment(1L);
  }
}
