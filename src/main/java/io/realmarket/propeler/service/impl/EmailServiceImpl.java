package io.realmarket.propeler.service.impl;

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
import java.util.Map;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  public static final String USERNAME = "username";
  public static final String ACTIVATION_TOKEN = "activationToken";
  public static final String USERNAME_LIST = "username_list";
  public static final String RESET_TOKEN = "resetToken";
  public static final String EMAIL_CHANGE_TOKEN = "changeEmailToken";

  private static final String CONTACT_US_EMAIL = "contactUsEmail";
  private static final String LOGO = "logo";
  private static final String ACTIVATION_LINK = "activationLink";
  private static final String RESET_PASSWORD_LINK = "resetPasswordLink";
  private static final String LOGO_PATH = "/static/images/logo.png";

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
    emailMessageDto.setReceiver(mailContentHolder.getEmail());
    String subject = "";

    Map<String, Object> data = null;
    String templateName = null;

    switch (mailContentHolder.getType()) {
      case REGISTER:
        subject = "Propeler - Welcome";
        data = getRegistrationEmailData(mailContentHolder);
        templateName = "activateAccountMailTemplate";
        break;

      case RESET_PASSWORD:
        subject = "Propeler - Reset Password";
        data = getResetTokenEmailData(mailContentHolder);
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
        templateName = "secretCHangeMailTemplate";
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

  private Map<String, Object> getResetTokenEmailData(MailContentHolder mailContentHolder) {
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

  private Map<String, Object> getBasicEmailData() {
    Map<String, Object> data = new HashMap<>();
    data.put(LOGO, LOGO);
    return data;
  }

  private void sendMessage(EmailMessageDto emailMessageDto) {

    MimeMessage email = javaMailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");

      helper.setSubject(emailMessageDto.getSubject());
      helper.setTo(emailMessageDto.getReceiver());
      helper.setText(emailMessageDto.getText(), true);

      helper.addInline(LOGO, new ClassPathResource(LOGO_PATH), "image/png");

      log.debug("Trying to send e-mail message: {}", emailMessageDto);

      javaMailSender.send(email);

      log.info("E-mail sent to user {}", emailMessageDto.getReceiver());

    } catch (MessagingException e) {
      log.error("Problem with sending email: {}", e.getCause());
      throw new EmailSendingException(ExceptionMessages.EMAIL_SENDING_EXCEPTION + e.getCause());
    }
  }
}
