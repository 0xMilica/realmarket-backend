package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.exception.EmailSendingException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.MailContentBuilder;
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.service.util.dto.EmailMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  public static final String USERNAME = "username";
  public static final String ACTIVATION_TOKEN = "activationToken";
  public static final String USERNAME_LIST = "username_list";
  public static final String RESET_TOKEN = "resetToken";
  public static final String EMAIL_CHANGE_TOKEN = "changeEmailToken";
  public static final String CAMPAIGN = "campaign";
  public static final String CAMPAIGNS = "campaigns";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String REGISTRATION_TOKEN = "registrationToken";
  public static final String REJECTION_REASON = "rejectionReason";
  public static final String DATE = "date";

  private static final String CONTACT_US_EMAIL = "contactUsEmail";
  private static final String REGISTRATION_LINK = "registrationLink";
  private static final String ACTIVATION_LINK = "activationLink";
  private static final String RESET_PASSWORD_LINK = "resetPasswordLink";
  private static final String DASHBOARD_LINK = "dashboardLink";

  private static final String PNG_IMAGE = "image/png";
  private static final String LOGO = "logo";
  private static final String LOGO_PATH = "/static/images/logo.png";
  private static final String CHECK_CIRCLE = "check_circle";
  private static final String CHECK_CIRCLE_PATH = "/static/images/check_circle.png";
  private static final String WARNING = "warning";
  private static final String WARNING_PATH = "/static/images/warning.png";
  private static final String TWITTER = "twitter";
  private static final String TWITTER_PATH = "/static/images/twitter.png";
  private static final String FACEBOOK = "facebook";
  private static final String FACEBOOK_PATH = "/static/images/facebook.png";
  private static final String YOUTUBE = "youtube";
  private static final String YOUTUBE_PATH = "/static/images/youtube.png";
  private static final String LINKEDIN = "linkedin";
  private static final String LINKEDIN_PATH = "/static/images/linkedin.png";

  private static final String CONTACT_US_SUBJECT = "Realmarket team would like to hear from you!";

  private JavaMailSender javaMailSender;
  private MailContentBuilder mailContentBuilder;

  @Value(value = "${frontend.service.url}")
  private String frontendServiceUrlPath;

  @Value(value = "${app.email.contact_us}")
  private String contactMeEmail;

  @Autowired
  public EmailServiceImpl(JavaMailSender javaMailSender, MailContentBuilder mailContentBuilder) {
    this.javaMailSender = javaMailSender;
    this.mailContentBuilder = mailContentBuilder;
  }

  @Async
  public void sendMailToUser(MailContentHolder mailContentHolder) {
    sendMessage(generateMailMessage(mailContentHolder));
  }

  /**
   * Uses MailContentHolder object to prepare EmailMessageDto object with a subject, receiver and
   * email content (body).
   *
   * @param mailContentHolder receiver email address, template type and params for template
   * @return EmailMessageDto
   */
  private EmailMessageDto generateMailMessage(MailContentHolder mailContentHolder) {
    EmailMessageDto emailMessageDto = new EmailMessageDto();
    List<String> emails = mailContentHolder.getEmails();
    emailMessageDto.setReceivers(emails.toArray(new String[emails.size()]));
    String subject = "";

    Map<String, Object> data;
    String templateName = null;

    emailMessageDto.setType(mailContentHolder.getType());
    switch (mailContentHolder.getType()) {
      case REGISTER:
        subject = "Propeler - Welcome";
        data = getRegistrationEmailData(mailContentHolder);
        templateName = "activateAccountMailTemplate";
        break;

      case RESET_PASSWORD:
        subject = "Propeler - Reset Password";
        data = getResetPasswordEmailData(mailContentHolder);
        templateName = "resetPasswordMailTemplate";
        break;

      case RECOVER_USERNAME:
        subject = "Propeler - Recover Username";
        data = getRecoverUsernameEmailData(mailContentHolder);
        templateName = "recoverUsernameMailTemplate";
        break;

      case CHANGE_EMAIL:
        subject = "Propeler - Change email";
        data = getChangeEmailEmailData(mailContentHolder);
        templateName = "requestEmailChangeTemplate";
        break;

      case SECRET_CHANGE:
        subject = "Propeler - Secret Changed";
        data = getBasicEmailData();
        templateName = "secretChangeMailTemplate";
        break;

      case ACCOUNT_BLOCKED:
        subject = "Propeler - Account blocked";
        data = getBlockedAccountData(mailContentHolder);
        templateName = "accountBlockedEmailTemplate";
        break;

      case NEW_CAMPAIGN_OPPORTUNITY:
        subject = "Propeler - New campaign opportunity";
        data = getData(mailContentHolder);
        templateName = "newCampaignEmailTemplate";
        break;

      case NEW_CAMPAIGN_OPPORTUNITIES:
        subject = "Propeler - New campaign opportunities";
        data = getData(mailContentHolder);
        templateName = "newCampaignOpportunitiesEmailTemplate";
        break;

      case FUNDRAISING_PROPOSAL_APPROVAL:
        subject = "Propoler - Fundraising proposal approval";
        data = getFundraisingProposalApprovalData(mailContentHolder);
        templateName = "acceptFundraisingProposalTemplate";
        break;

      case FUNDRAISING_PROPOSAL_REJECTION:
        subject = "Propeler - Fundraising proposal rejection";
        data = getRejectionData(mailContentHolder);
        data.put(WARNING, WARNING);
        templateName = "rejectFundraisingProposalTemplate";
        break;

      case ACCEPT_CAMPAIGN:
        subject = "Propeler - Campaign accepted";
        data = getCampaignAcceptData(mailContentHolder);
        templateName = "acceptCampaignTemplate";
        break;

      case REJECT_CAMPAIGN:
        subject = "Propeler - Campaign rejected";
        data = getRejectionData(mailContentHolder);
        templateName = "rejectCampaignTemplate";
        break;

      case KYC_UNDER_REVIEW:
        subject = "Propeler - KYC regulation compliance";
        data = getKYCUnderReviewData(mailContentHolder);
        templateName = "underReviewKYCTemplate";
        break;

      case KYC_APPROVAL:
        subject = "Propeler - KYC accepted";
        data = getApprovalData(mailContentHolder);
        templateName = "acceptKYCTemplate";
        break;

      case KYC_REJECTION:
        subject = "Propeler - KYC rejection";
        data = getRejectionData(mailContentHolder);
        templateName = "rejectKYCTemplate";
        break;

      default:
        data = new HashMap<>();
        break;
    }
    data.put(CONTACT_US_EMAIL, "mailto:" + contactMeEmail + "?subject=" + CONTACT_US_SUBJECT);

    emailMessageDto.setSubject(subject);
    emailMessageDto.setText(mailContentBuilder.build(data, templateName));

    return emailMessageDto;
  }

  private Map<String, Object> getBasicEmailData() {
    Map<String, Object> data = new HashMap<>();
    data.put(LOGO, LOGO);
    return data;
  }

  private Map<String, Object> getSocialMediaData() {
    Map<String, Object> data = new HashMap<>();
    data.put(TWITTER, TWITTER);
    data.put(FACEBOOK, FACEBOOK);
    data.put(YOUTUBE, YOUTUBE);
    data.put(LINKEDIN, LINKEDIN);
    return data;
  }

  private Map<String, Object> getRegistrationEmailData(MailContentHolder mailContentHolder) {
    String activationToken = (String) mailContentHolder.getContent().get(ACTIVATION_TOKEN);
    if (activationToken == null) {
      throw new IllegalArgumentException(ExceptionMessages.INVALID_TOKEN_PROVIDED);
    }

    String activationLink =
        String.format(
            "%s/auth/confirm-registration?registrationToken=%s",
            frontendServiceUrlPath, activationToken);

    Map<String, Object> data = getBasicEmailData();
    data.put(USERNAME, mailContentHolder.getContent().get(USERNAME));
    data.put(ACTIVATION_LINK, activationLink);

    return data;
  }

  private Map<String, Object> getResetPasswordEmailData(MailContentHolder mailContentHolder) {
    String resetToken = (String) mailContentHolder.getContent().get(RESET_TOKEN);
    if (resetToken == null) {
      throw new IllegalArgumentException(ExceptionMessages.INVALID_TOKEN_PROVIDED);
    }

    String resetPasswordLink =
        String.format("%s/auth/change-password?reset=%s", frontendServiceUrlPath, resetToken);

    Map<String, Object> data = getBasicEmailData();
    data.put(RESET_PASSWORD_LINK, resetPasswordLink);

    return data;
  }

  private Map<String, Object> getRecoverUsernameEmailData(MailContentHolder mailContentHolder) {
    Map<String, Object> data = getBasicEmailData();
    data.put(USERNAME_LIST, mailContentHolder.getContent().get(USERNAME_LIST));

    return data;
  }

  private Map<String, Object> getChangeEmailEmailData(MailContentHolder mailContentHolder) {
    String token = (String) mailContentHolder.getContent().get(EMAIL_CHANGE_TOKEN);
    if (token == null) {
      throw new IllegalArgumentException(ExceptionMessages.INVALID_TOKEN_PROVIDED);
    }

    String changeEmailLink =
        String.format("%s/auth/change-email?emailChangeToken=%s", frontendServiceUrlPath, token);

    Map<String, Object> data = getBasicEmailData();
    data.put(EMAIL_CHANGE_TOKEN, changeEmailLink);

    return data;
  }

  private Map<String, Object> getBlockedAccountData(MailContentHolder mailContentHolder) {
    Map<String, Object> data = getBasicEmailData();
    data.put(USERNAME, mailContentHolder.getContent().get(USERNAME));
    return data;
  }

  private Map<String, Object> getCampaignAcceptData(MailContentHolder mailContentHolder) {
    Map<String, Object> data = getBasicEmailData();
    data.putAll(mailContentHolder.getContent());
    data.putAll(getSocialMediaData());
    data.put(CHECK_CIRCLE, CHECK_CIRCLE);
    data.put(DASHBOARD_LINK, frontendServiceUrlPath);

    return data;
  }

  private Map<String, Object> getFundraisingProposalApprovalData(
      MailContentHolder mailContentHolder) {
    String registrationToken = (String) mailContentHolder.getContent().get(REGISTRATION_TOKEN);
    if (registrationToken == null) {
      throw new IllegalArgumentException(ExceptionMessages.INVALID_TOKEN_PROVIDED);
    }

    String registrationLink =
        String.format("%s/auth/register/%s", frontendServiceUrlPath, registrationToken);
    Map<String, Object> data = getBasicEmailData();
    data.putAll(mailContentHolder.getContent());
    data.putAll(getSocialMediaData());
    data.put(CHECK_CIRCLE, CHECK_CIRCLE);
    data.put(REGISTRATION_LINK, registrationLink);

    return data;
  }

  private Map<String, Object> getApprovalData(MailContentHolder mailContentHolder) {
    Map<String, Object> data = getBasicEmailData();
    data.putAll(mailContentHolder.getContent());
    data.putAll(getSocialMediaData());
    data.put(CHECK_CIRCLE, CHECK_CIRCLE);

    return data;
  }

  private Map<String, Object> getRejectionData(MailContentHolder mailContentHolder) {
    Map<String, Object> data = getBasicEmailData();
    data.putAll(mailContentHolder.getContent());
    data.putAll(getSocialMediaData());
    data.put(WARNING, WARNING);

    return data;
  }

  private Map<String, Object> getKYCUnderReviewData(MailContentHolder mailContentHolder) {
    Map<String, Object> data = getBasicEmailData();
    data.putAll(mailContentHolder.getContent());
    data.putAll(getSocialMediaData());

    return data;
  }

  private Map<String, Object> getData(MailContentHolder mailContentHolder) {
    Map<String, Object> data = getBasicEmailData();
    data.putAll(mailContentHolder.getContent());
    return data;
  }

  private void sendMessage(EmailMessageDto emailMessageDto) {

    MimeMessage email = javaMailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");

      helper.setSubject(emailMessageDto.getSubject());
      helper.setTo(emailMessageDto.getReceivers());
      helper.setText(emailMessageDto.getText(), true);

      addInlineContent(helper, emailMessageDto.getType());

      log.debug("Trying to send e-mail message: {}", emailMessageDto);

      javaMailSender.send(email);

      log.info("E-mail sent to users");

    } catch (MessagingException e) {
      log.error("Problem with sending email: {}", e.getCause());
      throw new EmailSendingException(ExceptionMessages.EMAIL_SENDING_EXCEPTION + e.getCause());
    }
  }

  private void addInlineContent(MimeMessageHelper helper, EmailType emailType)
      throws MessagingException {
    helper.addInline(LOGO, new ClassPathResource(LOGO_PATH), PNG_IMAGE);

    switch (emailType) {
      case KYC_APPROVAL:
      case ACCEPT_CAMPAIGN:
      case FUNDRAISING_PROPOSAL_APPROVAL:
        helper.addInline(TWITTER, new ClassPathResource(TWITTER_PATH), PNG_IMAGE);
        helper.addInline(FACEBOOK, new ClassPathResource(FACEBOOK_PATH), PNG_IMAGE);
        helper.addInline(YOUTUBE, new ClassPathResource(YOUTUBE_PATH), PNG_IMAGE);
        helper.addInline(LINKEDIN, new ClassPathResource(LINKEDIN_PATH), PNG_IMAGE);
        helper.addInline(CHECK_CIRCLE, new ClassPathResource(CHECK_CIRCLE_PATH), PNG_IMAGE);
        break;
      case KYC_REJECTION:
      case REJECT_CAMPAIGN:
      case FUNDRAISING_PROPOSAL_REJECTION:
        helper.addInline(TWITTER, new ClassPathResource(TWITTER_PATH), PNG_IMAGE);
        helper.addInline(FACEBOOK, new ClassPathResource(FACEBOOK_PATH), PNG_IMAGE);
        helper.addInline(YOUTUBE, new ClassPathResource(YOUTUBE_PATH), PNG_IMAGE);
        helper.addInline(LINKEDIN, new ClassPathResource(LINKEDIN_PATH), PNG_IMAGE);
        helper.addInline(WARNING, new ClassPathResource(WARNING_PATH), PNG_IMAGE);
        break;
      case KYC_UNDER_REVIEW:
        helper.addInline(TWITTER, new ClassPathResource(TWITTER_PATH), PNG_IMAGE);
        helper.addInline(FACEBOOK, new ClassPathResource(FACEBOOK_PATH), PNG_IMAGE);
        helper.addInline(YOUTUBE, new ClassPathResource(YOUTUBE_PATH), PNG_IMAGE);
        helper.addInline(LINKEDIN, new ClassPathResource(LINKEDIN_PATH), PNG_IMAGE);
        break;
      default:
        break;
    }
  }
}
