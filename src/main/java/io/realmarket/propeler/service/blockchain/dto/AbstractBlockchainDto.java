package io.realmarket.propeler.service.blockchain.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.realmarket.propeler.service.blockchain.dto.campaign.*;
import io.realmarket.propeler.service.blockchain.dto.company.CompanyEditRequestDto;
import io.realmarket.propeler.service.blockchain.dto.company.CompanyRegistrationDto;
import io.realmarket.propeler.service.blockchain.dto.company.CompanyShareholderDto;
import io.realmarket.propeler.service.blockchain.dto.company.CompanyUpdateShareholdersDto;
import io.realmarket.propeler.service.blockchain.dto.investment.InvestmentChangeStateDto;
import io.realmarket.propeler.service.blockchain.dto.investment.InvestmentDto;
import io.realmarket.propeler.service.blockchain.dto.investment.payment.PaymentDto;
import io.realmarket.propeler.service.blockchain.dto.user.UserEmailChangeDto;
import io.realmarket.propeler.service.blockchain.dto.user.UserPasswordChangeDto;
import io.realmarket.propeler.service.blockchain.dto.user.UserRegenerationOfRecoveryDto;
import io.realmarket.propeler.service.blockchain.dto.user.UserRegistrationDto;
import io.realmarket.propeler.service.blockchain.dto.user.kyc.KYCChangeStateDto;
import io.realmarket.propeler.service.blockchain.dto.user.kyc.KYCRequestForReviewDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CampaignChangeStateDto.class, name = "CampaignChangeStateDto"),
  @JsonSubTypes.Type(value = CampaignClosingDto.class, name = "CampaignClosingDto"),
  @JsonSubTypes.Type(
      value = CampaignDocumentAccessRequestDto.class,
      name = "CampaignDocumentAccessRequestDto"),
  @JsonSubTypes.Type(
      value = CampaignDocumentAccessRequestStateChangeDto.class,
      name = "CampaignDocumentAccessRequestStateChangeDto"),
  @JsonSubTypes.Type(
      value = CampaignSubmissionForReviewDto.class,
      name = "CampaignSubmissionForReviewDto"),
  @JsonSubTypes.Type(value = CompanyEditRequestDto.class, name = "CompanyEditRequestDto"),
  @JsonSubTypes.Type(value = CompanyRegistrationDto.class, name = "CompanyRegistrationDto"),
  @JsonSubTypes.Type(value = CompanyShareholderDto.class, name = "CompanyShareholderDto"),
  @JsonSubTypes.Type(
      value = CompanyUpdateShareholdersDto.class,
      name = "CompanyUpdateShareholdersDto"),
  @JsonSubTypes.Type(value = PaymentDto.class, name = "PaymentDto"),
  @JsonSubTypes.Type(value = InvestmentChangeStateDto.class, name = "InvestmentChangeStateDto"),
  @JsonSubTypes.Type(value = InvestmentDto.class, name = "InvestmentDto"),
  @JsonSubTypes.Type(value = KYCChangeStateDto.class, name = "KYCChangeStateDto"),
  @JsonSubTypes.Type(value = KYCRequestForReviewDto.class, name = "KYCRequestForReviewDto"),
  @JsonSubTypes.Type(value = UserEmailChangeDto.class, name = "UserEmailChangeDto"),
  @JsonSubTypes.Type(value = UserPasswordChangeDto.class, name = "UserPasswordChangeDto"),
  @JsonSubTypes.Type(
      value = UserRegenerationOfRecoveryDto.class,
      name = "UserRegenerationOfRecoveryDto"),
  @JsonSubTypes.Type(value = UserRegistrationDto.class, name = "UserRegistrationDto")
})
public abstract class AbstractBlockchainDto {
  protected Long userId;
  protected String IP;
  protected Long timestamp;
  protected String name;
}
