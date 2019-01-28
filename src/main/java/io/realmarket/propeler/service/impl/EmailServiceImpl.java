package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.EmailDto;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.exception.EmailSendingException;
import io.realmarket.propeler.service.util.MailContentBuilder;
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

  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String ACTIVATION_TOKEN = "activationToken";
  private static final String LOGO = "logo";
  private static final String RESET_TOKEN = "resetToken";

  private static final String ACTIVATION_LINK = "activationLink";
  private static final String RESET_PASSWORD_LINK = "resetPasswordLink";

  private static final String LOGO_PATH = "/static/images/logo.png";

  private JavaMailSender javaMailSender;
  private MailContentBuilder mailContentBuilder;

  @Value(value = "${frontend.service.url}")
  private String frontendServiceUrlPath;

  @Autowired
  public EmailServiceImpl(JavaMailSender javaMailSender, MailContentBuilder mailContentBuilder) {
    this.javaMailSender = javaMailSender;
    this.mailContentBuilder = mailContentBuilder;
  }

  @Async
  public void sendMailToUser(EmailDto emailDto) {
    sendMessage(generateMailMessage(emailDto));
  }

  /**
   * Uses EmailDto object to prepare EmailMessageDto object with a subject, receiver and email
   * content (body).
   *
   * @param emailDto receiver email address, template type and params for template
   * @return EmailMessageDto
   */
  private EmailMessageDto generateMailMessage(EmailDto emailDto) {
    EmailMessageDto emailMessageDto = new EmailMessageDto();
    emailMessageDto.setReceiver(emailDto.getEmail());
    String subject = "";

    Map<String, Object> data = null;
    String templateName = null;

    switch (emailDto.getType()) {
      case REGISTER:
        subject = "RealMarket - Welcome";
        data = getRegistrationEmailData(emailDto);
        templateName = "activateAccountMailTemplate";
        break;

      case RESET_PASSWORD:
        subject = "RealMarket - Reset Password";
        data = getResetTokenEmailData(emailDto);
        templateName = "resetPasswordMailTemplate";
        break;

      default:
    }

    emailMessageDto.setSubject(subject);
    emailMessageDto.setText(mailContentBuilder.build(data, templateName));

    return emailMessageDto;
  }

  private Map<String, Object> getRegistrationEmailData(EmailDto emailDto) {
    String activationToken = (String) emailDto.getContent().get(ACTIVATION_TOKEN);
    if (activationToken == null) {
      throw new IllegalArgumentException("activationToken is not provided");
    }

    String activationLink =
        String.format("%s/auth/activate-account?auth=%s", frontendServiceUrlPath, activationToken);

    Map<String, Object> data = new HashMap<>();
    data.put(LOGO, LOGO);
    data.put(FIRST_NAME, emailDto.getContent().get(FIRST_NAME));
    data.put(LAST_NAME, emailDto.getContent().get(LAST_NAME));
    data.put(ACTIVATION_LINK, activationLink);

    return data;
  }

  private Map<String, Object> getResetTokenEmailData(EmailDto emailDto) {
    String resetToken = (String) emailDto.getContent().get(RESET_TOKEN);
    if (resetToken == null) {
      throw new IllegalArgumentException("resetToken is not provided");
    }

    String resetPasswordLink =
        String.format("%s/auth/change-password?reset=%s", frontendServiceUrlPath, resetToken);

    Map<String, Object> data = new HashMap<>();
    data.put(LOGO, LOGO);
    data.put(RESET_PASSWORD_LINK, resetPasswordLink);

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
      throw new EmailSendingException("Messaging exception: " + e.getCause());
    }
  }
}
