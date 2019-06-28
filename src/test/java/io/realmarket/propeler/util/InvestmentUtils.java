package io.realmarket.propeler.util;

import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.InvestmentState;
import io.realmarket.propeler.model.enums.InvestmentStateName;

import java.math.BigDecimal;
import java.time.Instant;

public class InvestmentUtils {

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
  public static final Instant TEST_INVESTMENT_PAYMENT_DATE = Instant.now().minusMillis(WEEK);

  public static final Investment TEST_INVESTMENT_INITIAL =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_INITIAL_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .build();

  public static final Investment TEST_INVESTMENT_PAID_NOT_REVOCABLE =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_PAID_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(TEST_INVESTMENT_PAYMENT_DATE)
          .build();

  public static final Investment TEST_INVESTMENT_PAID_REVOCABLE =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_PAID_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now())
          .build();

  public static final Investment TEST_INVESTMENT_NOT_PAID_REVOCABLE =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_INITIAL_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(TEST_INVESTMENT_PAYMENT_DATE)
          .build();

  public static final Investment TEST_INVESTMENT_REVOKED =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_REVOKED_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now())
          .build();

  public static final Investment TEST_INVESTMENT_APPROVED =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_APPROVED_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(TEST_INVESTMENT_PAYMENT_DATE)
          .build();

  public static final Investment TEST_INVESTMENT_REJECTED =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_REJECTED_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(TEST_INVESTMENT_PAYMENT_DATE)
          .build();
}
