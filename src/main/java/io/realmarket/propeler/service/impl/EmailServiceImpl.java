package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.exception.EmailSendingException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.email.EmailContentBuilder;
import io.realmarket.propeler.service.util.email.EmailMessageFactory;
import io.realmarket.propeler.service.util.email.message.AbstractEmailMessage;
import io.realmarket.propeler.service.util.email.message.EmailAttachment;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.realmarket.propeler.service.util.email.Parameters.*;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  private static final String PNG_IMAGE = "image/png";

  @Value(value = "${resource.base_path.images}")
  private String basePath;

  @Value(value = "${resource.relative_path.images.logo}")
  private String logoPath;

  @Value(value = "${resource.relative_path.images.illustration}")
  private String illustrationPath;

  @Value(value = "${resource.relative_path.images.check_circle}")
  private String checkCirclePath;

  @Value(value = "${resource.relative_path.images.warning}")
  private String warningPath;

  @Value(value = "${resource.relative_path.images.twitter}")
  private String twitterPath;

  @Value(value = "${resource.relative_path.images.facebook}")
  private String facebookPath;

  @Value(value = "${resource.relative_path.images.youtube}")
  private String youtubePath;

  @Value(value = "${resource.relative_path.images.linkedin}")
  private String linkedinPath;

  private JavaMailSender javaMailSender;
  private EmailContentBuilder emailContentBuilder;
  private EmailMessageFactory emailMessageFactory;

  @Autowired
  public EmailServiceImpl(
      JavaMailSender javaMailSender,
      EmailContentBuilder emailContentBuilder,
      EmailMessageFactory emailMessageFactory) {
    this.javaMailSender = javaMailSender;
    this.emailContentBuilder = emailContentBuilder;
    this.emailMessageFactory = emailMessageFactory;
  }

  @Async
  public void sendEmailToUser(
      EmailType emailType, List<String> addressList, Map<String, Object> contentMap) {

    AbstractEmailMessage emailMessage =
        emailMessageFactory.buildMessage(emailType, addressList, contentMap);

    sendMessage(emailType, emailMessage);
  }

  @Async
  public void sendEmailToUser(
      EmailType emailType,
      List<String> addressList,
      Map<String, Object> contentMap,
      EmailAttachment emailAttachment) {
    AbstractEmailMessage emailMessage =
        emailMessageFactory.buildMessage(emailType, addressList, contentMap, emailAttachment);

    sendMessage(emailType, emailMessage);
  }

  private void sendMessage(EmailType emailType, AbstractEmailMessage emailMessage) {
    MimeMessage email = generateMimeMessage(emailType, emailMessage);

    log.debug("Trying to send e-mail message: {}", emailMessage);
    javaMailSender.send(email);
    log.info("E-mail sent");
  }

  private MimeMessage generateMimeMessage(EmailType emailType, AbstractEmailMessage emailMessage) {
    MimeMessage email = javaMailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");

      helper.setSubject(emailMessage.getSubject());

      helper.setTo(emailMessage.getAddressList().toArray(new String[0]));
      helper.setText(
          emailContentBuilder.build(emailMessage.getData(), emailMessage.getTemplateName()), true);

      addInlineContent(helper, emailType);

      createTempFile(emailMessage, helper);
    } catch (MessagingException | IOException e) {
      log.error("Problem with sending email: {}", e.getCause());
      throw new EmailSendingException(ExceptionMessages.EMAIL_SENDING_EXCEPTION + e.getCause());
    }

    return email;
  }

  private void createTempFile(AbstractEmailMessage emailMessage, MimeMessageHelper helper)
      throws IOException, MessagingException {
    EmailAttachment attachment = emailMessage.getAttachment();
    if (attachment != null) {
      File tempFile = File.createTempFile(attachment.getName(), attachment.getExtension(), null);
      try (FileOutputStream fos = new FileOutputStream(tempFile)) {
        fos.write(attachment.getFile());
        helper.addAttachment(attachment.getName(), tempFile);
      }
    }
  }

  private void addInlineContent(MimeMessageHelper helper, EmailType emailType)
      throws MessagingException {
    helper.addInline(LOGO, new ClassPathResource(basePath + logoPath), PNG_IMAGE);
    helper.addInline(TWITTER, new ClassPathResource(basePath + twitterPath), PNG_IMAGE);
    helper.addInline(FACEBOOK, new ClassPathResource(basePath + facebookPath), PNG_IMAGE);
    helper.addInline(YOUTUBE, new ClassPathResource(basePath + youtubePath), PNG_IMAGE);
    helper.addInline(LINKEDIN, new ClassPathResource(basePath + linkedinPath), PNG_IMAGE);

    switch (emailType) {
      case REGISTER:
      case ACCOUNT_BLOCKED:
      case RECOVER_USERNAME:
      case RESET_PASSWORD:
      case CHANGE_EMAIL:
        helper.addInline(
            ILLUSTRATION, new ClassPathResource(basePath + illustrationPath), PNG_IMAGE);
        break;
      case KYC_APPROVAL:
      case ACCEPT_CAMPAIGN:
      case FUNDRAISING_PROPOSAL_APPROVAL:
      case INVESTMENT_APPROVAL:
        helper.addInline(
            CHECK_CIRCLE, new ClassPathResource(basePath + checkCirclePath), PNG_IMAGE);
        break;
      case KYC_REJECTION:
      case REJECT_CAMPAIGN:
      case FUNDRAISING_PROPOSAL_REJECTION:
      case INVESTMENT_REJECTION:
        helper.addInline(WARNING, new ClassPathResource(basePath + warningPath), PNG_IMAGE);
        break;
      default:
        break;
    }
  }
}
