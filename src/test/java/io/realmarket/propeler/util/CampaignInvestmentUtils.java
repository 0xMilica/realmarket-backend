package io.realmarket.propeler.util;

import io.realmarket.propeler.model.CampaignInvestment;
import io.realmarket.propeler.model.InvestmentState;
import io.realmarket.propeler.model.enums.InvestmentStateName;

import java.math.BigDecimal;
import java.time.Instant;

public class CampaignInvestmentUtils {

  private static final long WEEK = 604800000L;
  public static final long INVESTMENT_ID = 1L;
  public static final InvestmentState TEST_INVESTMENT_INITIAL_STATE =
      InvestmentState.builder().name(InvestmentStateName.INITIAL).build();
  public static final InvestmentState TEST_INVESTMENT_PAID_STATE =
      InvestmentState.builder().name(InvestmentStateName.PAID).build();
  public static final InvestmentState TEST_INVESTMENT_REVOKED_STATE =
      InvestmentState.builder().name(InvestmentStateName.REVOKED).build();
  public static final InvestmentState TEST_INVESTMENT_APPROVED_STATE =
      InvestmentState.builder().name(InvestmentStateName.APPROVED).build();
  public static final InvestmentState TEST_INVESTMENT_REJECTED_STATE =
      InvestmentState.builder().name(InvestmentStateName.REJECTED).build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_INITIAL =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_INITIAL_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_PAID_NOT_REVOCABLE =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_PAID_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now().minusMillis(WEEK))
          .build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_PAID_REVOCABLE =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_PAID_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now())
          .build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_NOT_PAID_REVOCABLE =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_INITIAL_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now().minusMillis(WEEK))
          .build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_REVOKED =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_REVOKED_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now())
          .build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_APPROVED =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_APPROVED_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now().minusMillis(WEEK))
          .build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_REJECTED =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_REJECTED_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now().minusMillis(WEEK))
          .build();
}
