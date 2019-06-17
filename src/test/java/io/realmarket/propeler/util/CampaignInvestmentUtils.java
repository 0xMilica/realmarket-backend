package io.realmarket.propeler.util;

import io.realmarket.propeler.model.CampaignInvestment;
import io.realmarket.propeler.model.InvestmentState;
import io.realmarket.propeler.model.enums.InvestmentStateName;

import java.math.BigDecimal;

public class CampaignInvestmentUtils {

  public static final InvestmentState TEST_INVESTMENT_PENDING_STATE =
      InvestmentState.builder().name(InvestmentStateName.PENDING).build();
  public static final InvestmentState TEST_INVESTMENT_APPROVED_STATE =
      InvestmentState.builder().name(InvestmentStateName.APPROVED).build();
  public static final InvestmentState TEST_INVESTMENT_DECLINED_STATE =
      InvestmentState.builder().name(InvestmentStateName.DECLINED).build();
  public static final InvestmentState TEST_INVESTMENT_CANCELLED_STATE =
      InvestmentState.builder().name(InvestmentStateName.CANCELLED).build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_PENDING =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_PENDING_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_APPROVED =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_APPROVED_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_DECLINED =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_DECLINED_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .build();

  public static final CampaignInvestment TEST_CAMPAIGN_INVESTMENT_CANCELLED =
      CampaignInvestment.builder()
          .campaign(CampaignUtils.TEST_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_CANCELLED_STATE)
          .auth(AuthUtils.TEST_AUTH_INVESTOR)
          .investedAmount(BigDecimal.valueOf(100))
          .build();
}
