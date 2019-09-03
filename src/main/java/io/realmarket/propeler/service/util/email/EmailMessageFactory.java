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

import java.util.List;
import java.util.Map;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.EMAIL_SENDING_EXCEPTION;
import static io.realmarket.propeler.service.util.email.Parameters.INVOICE_NUMBER;
import static io.realmarket.propeler.service.util.email.Parameters.PROFORMA_INVOICE_NUMBER;
import static io.realmarket.propeler.service.util.email.Values.*;

@Component
@Slf4j
public class EmailMessageFactory {
  @Value(value = "${frontend.service.url}")
  private String frontendServiceUrlPath;

  @Value(value = "${email.template.user.register}")
  private String registerEmailTemplate;

  @Value(value = "${email.template.user.reset_password}")
  private String resetPasswordEmailTemplate;

  @Value(value = "${email.template.user.recover_username}")
  private String recoverUsernameMailTemplate;

  @Value(value = "${email.template.user.change_email}")
  private String requestEmailChangeTemplate;

  @Value(value = "${email.template.user.secret_change}")
  private String secretChangeMailTemplate;

  @Value(value = "${email.template.user.account_blocked}")
  private String accountBlockedEmailTemplate;

  @Value(value = "${email.template.campaign.new_opportunity}")
  private String newCampaignOpportunityEmailTemplate;

  @Value(value = "${email.template.campaign.new_opportunities}")
  private String newCampaignOpportunitiesEmailTemplate;

  @Value(value = "${email.template.campaign.fundraising_proposal_approval}")
  private String acceptFundraisingProposalTemplate;

  @Value(value = "${email.template.campaign.fundraising_proposal_rejection}")
  private String rejectFundraisingProposalTemplate;

  @Value(value = "${email.template.campaign.approval}")
  private String acceptCampaignTemplate;

  @Value(value = "${email.template.campaign.rejection}")
  private String rejectCampaignTemplate;

  @Value(value = "${email.template.kyc.under_review}")
  private String underReviewKYCTemplate;

  @Value(value = "${email.template.kyc.approval}")
  private String acceptKYCTemplate;

  @Value(value = "${email.template.kyc.rejection}")
  private String rejectKYCTemplate;

  @Value(value = "${email.template.investment.approval}")
  private String acceptCampaignInvestmentTemplate;

  @Value(value = "${email.template.investment.rejection}")
  private String rejectInvestmentTemplate;

  @Value(value = "${email.template.investment.proforma_invoice}")
  private String proformaInvoiceMailTemplate;

  @Value(value = "${email.template.investment.invoice}")
  private String invoiceMailTemplate;

  public AbstractEmailMessage buildMessage(
      EmailType emailType, List<String> addressList, Map<String, Object> contentMap) {
    switch (emailType) {
      case REGISTER:
        return new RegisterMessage(
            addressList,
            contentMap,
            REGISTER_SUBJECT,
            registerEmailTemplate,
            frontendServiceUrlPath);
      case RESET_PASSWORD:
        return new ResetPasswordMessage(
            addressList,
            contentMap,
            RESET_PASSWORD_SUBJECT,
            resetPasswordEmailTemplate,
            frontendServiceUrlPath);
      case RECOVER_USERNAME:
        return new SimpleMessage(
            addressList, contentMap, RECOVER_USERNAME_SUBJECT, recoverUsernameMailTemplate);
      case CHANGE_EMAIL:
        return new ChangeEmailMessage(
            addressList,
            contentMap,
            CHANGE_EMAIL_SUBJECT,
            requestEmailChangeTemplate,
            frontendServiceUrlPath);
      case SECRET_CHANGE:
        return new SimpleMessage(
            addressList, contentMap, SECRET_CHANGE_SUBJECT, secretChangeMailTemplate);
      case ACCOUNT_BLOCKED:
        return new SimpleMessage(
            addressList, contentMap, ACCOUNT_BLOCKED_SUBJECT, accountBlockedEmailTemplate);
      case NEW_CAMPAIGN_OPPORTUNITY:
        return new SimpleMessage(
            addressList,
            contentMap,
            NEW_CAMPAIGN_OPPORTUNITY_SUBJECT,
            newCampaignOpportunityEmailTemplate);
      case NEW_CAMPAIGN_OPPORTUNITIES:
        return new SimpleMessage(
            addressList,
            contentMap,
            NEW_CAMPAIGN_OPPORTUNITIES_SUBJECT,
            newCampaignOpportunitiesEmailTemplate);
      case FUNDRAISING_PROPOSAL_APPROVAL:
        return new FundraisingApprovalMessage(
            addressList,
            contentMap,
            FUNDRAISING_APPROVAL_SUBJECT,
            acceptFundraisingProposalTemplate,
            frontendServiceUrlPath);
      case FUNDRAISING_PROPOSAL_REJECTION:
        return new RejectionMessage(
            addressList,
            contentMap,
            FUNDRAISING_REJECTION_SUBJECT,
            rejectFundraisingProposalTemplate);
      case ACCEPT_CAMPAIGN:
        return new ApprovalMessage(
            addressList,
            contentMap,
            ACCEPT_CAMPAIGN_SUBJECT,
            acceptCampaignTemplate,
            frontendServiceUrlPath);
      case REJECT_CAMPAIGN:
        return new RejectionMessage(
            addressList, contentMap, REJECT_CAMPAIGN_SUBJECT, rejectCampaignTemplate);

      case KYC_UNDER_REVIEW:
        return new SimpleMessage(
            addressList, contentMap, KYC_UNDER_REVIEW_SUBJECT, underReviewKYCTemplate);
      case KYC_APPROVAL:
        return new ApprovalMessage(
            addressList,
            contentMap,
            KYC_APPROVAL_SUBJECT,
            acceptKYCTemplate,
            frontendServiceUrlPath);
      case KYC_REJECTION:
        return new RejectionMessage(
            addressList, contentMap, KYC_REJECTION_SUBJECT, rejectKYCTemplate);
      case INVESTMENT_APPROVAL:
        return new InvestmentApprovalMessage(
            addressList,
            contentMap,
            INVESTMENT_APPROVAL_SUBJECT,
            acceptCampaignInvestmentTemplate,
            frontendServiceUrlPath);
      case INVESTMENT_REJECTION:
        return new RejectionMessage(
            addressList, contentMap, INVESTMENT_REJECTION_SUBJECT, rejectInvestmentTemplate);

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
