package io.realmarket.propeler.service.util.email;

import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.service.util.email.message.AbstractEmailMessage;
import io.realmarket.propeler.service.util.email.message.EmailAttachment;
import io.realmarket.propeler.service.util.email.message.custom.campaign.FundraisingApprovalMessage;
import io.realmarket.propeler.service.util.email.message.custom.campaign.InvestmentApprovalMessage;
import io.realmarket.propeler.service.util.email.message.custom.user.ChangeEmailMessage;
import io.realmarket.propeler.service.util.email.message.custom.user.RegisterMessage;
import io.realmarket.propeler.service.util.email.message.custom.user.ResetPasswordMessage;
import io.realmarket.propeler.service.util.email.message.general.ApprovalMessage;
import io.realmarket.propeler.service.util.email.message.general.RejectionMessage;
import io.realmarket.propeler.service.util.email.message.general.SimpleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.EMAIL_SENDING_EXCEPTION;
import static io.realmarket.propeler.service.util.email.Parameters.*;
import static io.realmarket.propeler.service.util.email.Values.*;

@Component
@Slf4j
public class EmailMessageFactory {
  @Value(value = "${email.template.user.register}")
  private String registerMailTemplate;

  @Value(value = "${email.template.user.reset_password}")
  private String resetPasswordMailTemplate;

  @Value(value = "${email.template.user.recover_username}")
  private String recoverUsernameMailTemplate;

  @Value(value = "${email.template.user.change_email}")
  private String requestEmailChangeMailTemplate;

  @Value(value = "${email.template.user.secret_change}")
  private String secretChangeMailTemplate;

  @Value(value = "${email.template.user.account_blocked}")
  private String accountBlockedMailTemplate;

  @Value(value = "${email.template.campaign.new_opportunity}")
  private String newCampaignOpportunityMailTemplate;

  @Value(value = "${email.template.campaign.new_opportunities}")
  private String newCampaignOpportunitiesMailTemplate;

  @Value(value = "${email.template.campaign.fundraising_proposal_approval}")
  private String acceptFundraisingProposalMailTemplate;

  @Value(value = "${email.template.campaign.fundraising_proposal_rejection}")
  private String rejectFundraisingProposalMailTemplate;

  @Value(value = "${email.template.campaign.approval}")
  private String acceptCampaignMailTemplate;

  @Value(value = "${email.template.campaign.rejection}")
  private String rejectCampaignMailTemplate;

  @Value(value = "${email.template.kyc.under_review}")
  private String underReviewKYCMailTemplate;

  @Value(value = "${email.template.kyc.approval}")
  private String acceptKYCMailTemplate;

  @Value(value = "${email.template.kyc.rejection}")
  private String rejectKYCMailTemplate;

  @Value(value = "${email.template.investment.approval}")
  private String acceptCampaignInvestmentMailTemplate;

  @Value(value = "${email.template.investment.rejection}")
  private String rejectCampaignInvestmentMailTemplate;

  @Value(value = "${email.template.investment.proforma_invoice}")
  private String proformaInvoiceMailTemplate;

  @Value(value = "${email.template.investment.invoice}")
  private String invoiceMailTemplate;

  @Value(value = "${frontend.service.url}")
  private String frontendServiceUrlPath;

  @Value(value = "${resource.link.platform}")
  private String platformLink;

  @Value(value = "${resource.link.twitter}")
  private String twitterLink;

  @Value(value = "${resource.link.facebook}")
  private String facebookLink;

  @Value(value = "${resource.link.youtube}")
  private String youtubeLink;

  @Value(value = "${resource.link.linkedin}")
  private String linkedinLink;

  @Value(value = "${email.contact_us}")
  private String contactUsEmail;

  public AbstractEmailMessage buildMessage(
      EmailType emailType, List<String> addressList, Map<String, Object> contentMap) {
    contentMap.putAll(
        Stream.of(
                new SimpleEntry<>(PLATFORM_LINK, platformLink),
                new SimpleEntry<>(TWITTER_LINK, twitterLink),
                new SimpleEntry<>(FACEBOOK_LINK, facebookLink),
                new SimpleEntry<>(YOUTUBE_LINK, youtubeLink),
                new SimpleEntry<>(LINKEDIN_LINK, linkedinLink),
                new SimpleEntry<>(
                    CONTACT_US_EMAIL,
                    String.format("mailto:%s?subject=%s", contactUsEmail, CONTACT_US)))
            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
    switch (emailType) {
      case REGISTER:
        return new RegisterMessage(
            addressList,
            contentMap,
            REGISTER_SUBJECT,
            registerMailTemplate,
            frontendServiceUrlPath);
      case RESET_PASSWORD:
        return new ResetPasswordMessage(
            addressList,
            contentMap,
            RESET_PASSWORD_SUBJECT,
            resetPasswordMailTemplate,
            frontendServiceUrlPath);
      case RECOVER_USERNAME:
        contentMap.put(ILLUSTRATION, ILLUSTRATION);
        return new SimpleMessage(
            addressList, contentMap, RECOVER_USERNAME_SUBJECT, recoverUsernameMailTemplate);
      case CHANGE_EMAIL:
        return new ChangeEmailMessage(
            addressList,
            contentMap,
            CHANGE_EMAIL_SUBJECT,
            requestEmailChangeMailTemplate,
            frontendServiceUrlPath);
      case SECRET_CHANGE:
        return new SimpleMessage(
            addressList, contentMap, SECRET_CHANGE_SUBJECT, secretChangeMailTemplate);
      case ACCOUNT_BLOCKED:
        contentMap.put(ILLUSTRATION, ILLUSTRATION);
        return new SimpleMessage(
            addressList, contentMap, ACCOUNT_BLOCKED_SUBJECT, accountBlockedMailTemplate);
      case NEW_CAMPAIGN_OPPORTUNITY:
        return new SimpleMessage(
            addressList,
            contentMap,
            NEW_CAMPAIGN_OPPORTUNITY_SUBJECT,
            newCampaignOpportunityMailTemplate);
      case NEW_CAMPAIGN_OPPORTUNITIES:
        return new SimpleMessage(
            addressList,
            contentMap,
            NEW_CAMPAIGN_OPPORTUNITIES_SUBJECT,
            newCampaignOpportunitiesMailTemplate);
      case FUNDRAISING_PROPOSAL_APPROVAL:
        return new FundraisingApprovalMessage(
            addressList,
            contentMap,
            FUNDRAISING_APPROVAL_SUBJECT,
            acceptFundraisingProposalMailTemplate,
            frontendServiceUrlPath);
      case FUNDRAISING_PROPOSAL_REJECTION:
        return new RejectionMessage(
            addressList,
            contentMap,
            FUNDRAISING_REJECTION_SUBJECT,
            rejectFundraisingProposalMailTemplate);
      case ACCEPT_CAMPAIGN:
        return new ApprovalMessage(
            addressList,
            contentMap,
            ACCEPT_CAMPAIGN_SUBJECT,
            acceptCampaignMailTemplate,
            frontendServiceUrlPath);
      case REJECT_CAMPAIGN:
        return new RejectionMessage(
            addressList, contentMap, REJECT_CAMPAIGN_SUBJECT, rejectCampaignMailTemplate);
      case KYC_UNDER_REVIEW:
        return new SimpleMessage(
            addressList, contentMap, KYC_UNDER_REVIEW_SUBJECT, underReviewKYCMailTemplate);
      case KYC_APPROVAL:
        return new ApprovalMessage(
            addressList,
            contentMap,
            KYC_APPROVAL_SUBJECT,
            acceptKYCMailTemplate,
            frontendServiceUrlPath);
      case KYC_REJECTION:
        return new RejectionMessage(
            addressList, contentMap, KYC_REJECTION_SUBJECT, rejectKYCMailTemplate);
      case INVESTMENT_APPROVAL:
        return new InvestmentApprovalMessage(
            addressList,
            contentMap,
            INVESTMENT_APPROVAL_SUBJECT,
            acceptCampaignInvestmentMailTemplate,
            frontendServiceUrlPath);
      case INVESTMENT_REJECTION:
        return new RejectionMessage(
            addressList,
            contentMap,
            INVESTMENT_REJECTION_SUBJECT,
            rejectCampaignInvestmentMailTemplate);

      default:
        log.error("Cannot build message for proposed type!");
        throw new IllegalArgumentException(EMAIL_SENDING_EXCEPTION);
    }
  }

  public AbstractEmailMessage buildMessage(
      EmailType emailType,
      List<String> addressList,
      Map<String, Object> contentMap,
      EmailAttachment emailAttachment) {
    switch (emailType) {
      case INVOICE:
        return new SimpleMessage(
            addressList,
            contentMap,
            INVOICE_SUBJECT_BASE + contentMap.get(INVOICE_NUMBER),
            emailAttachment,
            invoiceMailTemplate);
      case PROFORMA_INVOICE:
        return new SimpleMessage(
            addressList,
            contentMap,
            PROFORMA_INVOICE_SUBJECT_BASE + contentMap.get(PROFORMA_INVOICE_NUMBER),
            emailAttachment,
            proformaInvoiceMailTemplate);

      default:
        log.error("Cannot build message for proposed type!");
        throw new IllegalArgumentException("Cannot build message for the proposed type!");
    }
  }
}
