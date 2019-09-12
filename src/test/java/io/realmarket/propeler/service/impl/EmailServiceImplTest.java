package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.service.exception.EmailSendingException;
import io.realmarket.propeler.service.util.email.EmailContentBuilder;
import io.realmarket.propeler.service.util.email.EmailMessageFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static io.realmarket.propeler.util.EmailUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmailServiceImpl.class)
public class EmailServiceImplTest {
  @Mock private JavaMailSender javaMailSender;
  @Mock private EmailContentBuilder emailContentBuilder;

  @InjectMocks private EmailServiceImpl emailService;

  @Mock private EmailMessageFactory emailMessageFactory;

  @Test
  public void SendMailToUser_Should_SendRegistrationEmail() throws Exception {
    MimeMessage mockedEmail = Mockito.mock(MimeMessage.class);
    HashMap<String, Object> mockedResetEmailData = new HashMap<>();

    when(javaMailSender.createMimeMessage()).thenReturn(mockedEmail);
    doNothing().when(javaMailSender).send(mockedEmail);
    when(emailContentBuilder.build(any(), anyString())).thenReturn(TEST_EMAIL_TEXT);
    when(emailMessageFactory.buildMessage(any(), any(), any())).thenReturn(REGISTER_MESSAGE);
    PowerMockito.whenNew(HashMap.class).withNoArguments().thenReturn(mockedResetEmailData);

    emailService.sendEmailToUser(
        EmailType.REGISTER, Arrays.asList(TEST_USER_EMAIL), TEST_EMAIL_DATA);

    verify(javaMailSender, Mockito.times(1)).send(mockedEmail);
  }

  @Test(expected = NullPointerException.class)
  public void SendMailToUser_Should_Throw_NullPointerException_WhenNoActivationToken() {
    emailService.sendEmailToUser(
        EmailType.REGISTER, Arrays.asList(TEST_USER_EMAIL), new HashMap<>());
  }

  @Test(expected = EmailSendingException.class)
  public void SendMailToUser_Should_Throw_EmailSendingException_WhenEmailNotSent()
      throws Exception {

    MimeMessageHelper mockedHelper = mock(MimeMessageHelper.class);
    MimeMessage mockedEmail = Mockito.mock(MimeMessage.class);

    when(javaMailSender.createMimeMessage()).thenReturn(mockedEmail);

    PowerMockito.whenNew(MimeMessageHelper.class).withAnyArguments().thenReturn(mockedHelper);
    doThrow(MessagingException.class).when(mockedHelper).setSubject(anyString());

    when(emailMessageFactory.buildMessage(any(), any(), any())).thenReturn(REGISTER_MESSAGE);

    emailService.sendEmailToUser(
        EmailType.KYC_UNDER_REVIEW, Arrays.asList(TEST_USER_EMAIL), TEST_EMAIL_DATA);
  }

  @Test
  public void SendMailToUser_Should_SendResetEmail() throws Exception {
    MimeMessage mockedEmail = Mockito.mock(MimeMessage.class);

    HashMap<String, Object> mockedResetEmailData = new HashMap<>();

    when(javaMailSender.createMimeMessage()).thenReturn(mockedEmail);
    doNothing().when(javaMailSender).send(mockedEmail);

    when(emailContentBuilder.build(any(), anyString())).thenReturn(TEST_EMAIL_TEXT);
    PowerMockito.whenNew(HashMap.class).withNoArguments().thenReturn(mockedResetEmailData);

    when(emailMessageFactory.buildMessage(any(), any(), any())).thenReturn(REGISTER_MESSAGE);

    emailService.sendEmailToUser(
        EmailType.RESET_PASSWORD,
        Arrays.asList(TEST_USER_EMAIL),
        Collections.singletonMap("resetToken", TEST_RESET_TOKEN));

    verify(javaMailSender, Mockito.times(1)).send(mockedEmail);
  }

  @Test(expected = NullPointerException.class)
  public void SendMailToUser_Should_Throw_NullPointerException_WhenNoResetToken() {
    emailService.sendEmailToUser(
        EmailType.RESET_PASSWORD, Arrays.asList(TEST_USER_EMAIL), new HashMap<>());
  }
}
