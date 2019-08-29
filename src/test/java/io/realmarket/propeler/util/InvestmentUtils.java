package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.OffPlatformInvestmentRequestDto;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.InvestmentState;
import io.realmarket.propeler.model.enums.InvestmentStateName;

import java.math.BigDecimal;
import java.time.Instant;

import static io.realmarket.propeler.util.AuthUtils.TEST_COUNTRY_CODE;
import static io.realmarket.propeler.util.AuthUtils.TEST_EMAIL;
import static io.realmarket.propeler.util.PersonUtils.TEST_COUNTRY;
import static io.realmarket.propeler.util.PersonUtils.TEST_PROFILE_PICTURE_URL;

public class InvestmentUtils {

  private static final long WEEK = 604800000L;
  public static final long INVESTMENT_ID = 1L;
  public static final BigDecimal TEST_INVESTED_AMOUNT = BigDecimal.valueOf(100);
  public static final String TEST_CURRENCY = "EUR";
  public static final Instant TEST_CREATION_DATE = Instant.now();
  public static final Instant TEST_PAYMENT_DATE = Instant.now();
  public static final String TEST_INVOICE_URL = "TEST_INVOICE_URL";

  public static final InvestmentState mockInvestmentState(InvestmentStateName name) {
    return InvestmentState.builder().name(name).build();
  }

  public static final InvestmentState TEST_INVESTMENT_INITIAL_STATE =
      InvestmentState.builder().name(InvestmentStateName.INITIAL).build();
  public static final InvestmentState TEST_INVESTMENT_OWNER_APPROVED_STATE =
      InvestmentState.builder().name(InvestmentStateName.OWNER_APPROVED).build();
  public static final InvestmentState TEST_INVESTMENT_OWNER_REJECTED_STATE =
      InvestmentState.builder().name(InvestmentStateName.OWNER_REJECTED).build();
  public static final InvestmentState TEST_INVESTMENT_PAID_STATE =
      InvestmentState.builder().name(InvestmentStateName.PAID).build();
  public static final InvestmentState TEST_INVESTMENT_REVOKED_STATE =
      InvestmentState.builder().name(InvestmentStateName.REVOKED).build();
  public static final InvestmentState TEST_INVESTMENT_AUDIT_APPROVED_STATE =
      InvestmentState.builder().name(InvestmentStateName.AUDIT_APPROVED).build();
  public static final InvestmentState TEST_INVESTMENT_AUDIT_REJECTED_STATE =
      InvestmentState.builder().name(InvestmentStateName.AUDIT_REJECTED).build();
  public static final Instant TEST_INVESTMENT_PAYMENT_DATE = Instant.now().minusMillis(WEEK);

  public static final Investment TEST_INVESTMENT_INITIAL =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(TEST_INVESTMENT_INITIAL_STATE)
          .person(PersonUtils.TEST_PERSON)
          .investedAmount(BigDecimal.valueOf(100))
          .build();

  public static Investment mockOwnerApprovedInvestment() {
    return Investment.builder()
        .id(INVESTMENT_ID)
        .campaign(CampaignUtils.TEST_ACTIVE_CAMPAIGN)
        .investmentState(mockInvestmentState(InvestmentStateName.OWNER_APPROVED))
        .person(PersonUtils.TEST_PERSON)
        .investedAmount(TEST_INVESTED_AMOUNT)
        .currency(TEST_CURRENCY)
        .creationDate(TEST_CREATION_DATE)
        .build();
  }

  public static Investment mockPaidInvestment() {
    return Investment.builder()
        .id(INVESTMENT_ID)
        .campaign(CampaignUtils.TEST_ACTIVE_CAMPAIGN)
        .investmentState(mockInvestmentState(InvestmentStateName.PAID))
        .person(PersonUtils.TEST_PERSON)
        .investedAmount(TEST_INVESTED_AMOUNT)
        .currency(TEST_CURRENCY)
        .creationDate(TEST_CREATION_DATE)
        .paymentDate(TEST_PAYMENT_DATE)
        .invoiceUrl(TEST_INVOICE_URL)
        .build();
  }

  public static final Investment TEST_INVESTMENT_PAID =
      Investment.builder()
          .campaign(CampaignUtils.TEST_ACTIVE_CAMPAIGN)
          .investmentState(mockInvestmentState(InvestmentStateName.PAID))
          .person(PersonUtils.TEST_PERSON)
          .investedAmount(BigDecimal.valueOf(100))
          .build();

  public static final Investment TEST_INVESTMENT_PAID_NOT_REVOCABLE =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(mockInvestmentState(InvestmentStateName.PAID))
          .person(PersonUtils.TEST_PERSON)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(TEST_INVESTMENT_PAYMENT_DATE)
          .build();

  public static final Investment TEST_INVESTMENT_PAID_REVOCABLE =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(mockInvestmentState(InvestmentStateName.PAID))
          .person(PersonUtils.TEST_PERSON)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now())
          .build();

  public static final Investment TEST_INVESTMENT_NOT_PAID_REVOCABLE =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(mockInvestmentState(InvestmentStateName.INITIAL))
          .person(PersonUtils.TEST_PERSON)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(TEST_INVESTMENT_PAYMENT_DATE)
          .build();

  public static final Investment TEST_INVESTMENT_REVOKED =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(mockInvestmentState(InvestmentStateName.REVOKED))
          .person(PersonUtils.TEST_PERSON)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(Instant.now())
          .build();

  public static final Investment TEST_INVESTMENT_AUDIT_APPROVED =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(mockInvestmentState(InvestmentStateName.AUDIT_APPROVED))
          .person(PersonUtils.TEST_PERSON)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(TEST_INVESTMENT_PAYMENT_DATE)
          .build();

  public static final Investment TEST_INVESTMENT_AUDIT_REJECTED =
      Investment.builder()
          .campaign(CampaignUtils.TEST_INVESTABLE_CAMPAIGN)
          .investmentState(mockInvestmentState(InvestmentStateName.AUDIT_REJECTED))
          .person(PersonUtils.TEST_PERSON)
          .investedAmount(BigDecimal.valueOf(100))
          .paymentDate(TEST_INVESTMENT_PAYMENT_DATE)
          .build();

  public static OffPlatformInvestmentRequestDto TEST_OFFPLATFORM_INVESTMENT =
      OffPlatformInvestmentRequestDto.builder()
          .email(TEST_EMAIL)
          .profilePictureUrl(TEST_PROFILE_PICTURE_URL)
          .countryOfResidence(TEST_COUNTRY_CODE)
          .investedAmount(BigDecimal.valueOf(100))
          .build();

  public static OffPlatformInvestmentRequestDto TEST_OFFPLATFORM_INVESTMENT_NEGATIVE_AMOUNT =
      OffPlatformInvestmentRequestDto.builder()
          .email(TEST_EMAIL)
          .profilePictureUrl(TEST_PROFILE_PICTURE_URL)
          .countryOfResidence(TEST_COUNTRY.toString())
          .investedAmount(BigDecimal.valueOf(-100))
          .build();

  public static OffPlatformInvestmentRequestDto
      TEST_OFFPLATFORM_INVESTMENT_AMOUNT_LESSER_THAN_MINIMUM =
          OffPlatformInvestmentRequestDto.builder()
              .email(TEST_EMAIL)
              .profilePictureUrl(TEST_PROFILE_PICTURE_URL)
              .countryOfResidence(TEST_COUNTRY.toString())
              .investedAmount(BigDecimal.valueOf(0))
              .build();

  public static OffPlatformInvestmentRequestDto
      TEST_OFFPLATFORM_INVESTMENT_AMOUNT_GREATER_THAN_MAXIMUM =
          OffPlatformInvestmentRequestDto.builder()
              .email(TEST_EMAIL)
              .profilePictureUrl(TEST_PROFILE_PICTURE_URL)
              .countryOfResidence(TEST_COUNTRY.toString())
              .investedAmount(BigDecimal.valueOf(1000))
              .build();
}
