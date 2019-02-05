package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.service.exception.EmailSendingException;
import io.realmarket.propeler.service.impl.EmailServiceImpl;
import io.realmarket.propeler.service.util.MailContentBuilder;
import io.realmarket.propeler.service.util.dto.EmailMessageDto;
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
import java.util.HashMap;

import static io.realmarket.propeler.unit.util.EmailUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmailServiceImpl.class)
public class EmailServiceImplTest {
  @Mock private JavaMailSender javaMailSender;
  @Mock private MailContentBuilder mailContentBuilder;

  @InjectMocks private EmailServiceImpl emailService;

  @Test
  public void SendMailToUser_Should_SendRegistrationEmail() throws Exception {
    MimeMessage mockedEmail = Mockito.mock(MimeMessage.class);
    HashMap<String, Object> mockedResetEmailData = new HashMap<>();
    EmailMessageDto mockedEmailMessageDto = new EmailMessageDto();

    PowerMockito.whenNew(EmailMessageDto.class).withNoArguments().thenReturn(mockedEmailMessageDto);
    when(javaMailSender.createMimeMessage()).thenReturn(mockedEmail);
    doNothing().when(javaMailSender).send(mockedEmail);
    when(mailContentBuilder.build(any(), anyString())).thenReturn(TEST_EMAIL_TEXT);
    PowerMockito.whenNew(HashMap.class).withNoArguments().thenReturn(mockedResetEmailData);

    emailService.sendMailToUser(TEST_VALID_REGISTRATION_EMAIL_DTO);

    verify(javaMailSender, Mockito.times(1)).send(mockedEmail);
    assertEquals(TEST_USER_EMAIL, mockedEmailMessageDto.getReceiver());
    assertEquals(TEST_ACTIVATION_SUBJECT, mockedEmailMessageDto.getSubject());
    assertEquals(TEST_EMAIL_TEXT, mockedEmailMessageDto.getText());
    assertEquals(3, mockedResetEmailData.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void SendMailToUser_Should_Throw_IllegalArgumentException_WhenNoActivationToken() {
    emailService.sendMailToUser(TEST_INVALID_REGISTRATION_EMAIL_DTO);
  }

  @Test(expected = EmailSendingException.class)
  public void SendMailToUser_Should_Throw_EmailSendingException_WhenEmailNotSent()
      throws Exception {
    EmailMessageDto mockedEmailMessageDto = new EmailMessageDto();
    MimeMessageHelper mockedHelper = mock(MimeMessageHelper.class);
    MimeMessage mockedEmail = Mockito.mock(MimeMessage.class);

    when(javaMailSender.createMimeMessage()).thenReturn(mockedEmail);
    PowerMockito.whenNew(EmailMessageDto.class).withNoArguments().thenReturn(mockedEmailMessageDto);
    PowerMockito.whenNew(MimeMessageHelper.class).withAnyArguments().thenReturn(mockedHelper);
    doThrow(MessagingException.class).when(mockedHelper).setSubject(anyString());

    emailService.sendMailToUser(TEST_VALID_REGISTRATION_EMAIL_DTO);
  }

  @Test
  public void SendMailToUser_Should_SendResetEmail() throws Exception {
    MimeMessage mockedEmail = Mockito.mock(MimeMessage.class);
    EmailMessageDto mockedEmailMessageDto = new EmailMessageDto();
    HashMap<String, Object> mockedResetEmailData = new HashMap<>();

    PowerMockito.whenNew(EmailMessageDto.class).withNoArguments().thenReturn(mockedEmailMessageDto);
    when(javaMailSender.createMimeMessage()).thenReturn(mockedEmail);
    doNothing().when(javaMailSender).send(mockedEmail);
    when(mailContentBuilder.build(any(), anyString())).thenReturn(TEST_EMAIL_TEXT);
    PowerMockito.whenNew(HashMap.class).withNoArguments().thenReturn(mockedResetEmailData);

    emailService.sendMailToUser(TEST_VALID_RESET_EMAIL_DTO);

    verify(javaMailSender, Mockito.times(1)).send(mockedEmail);
    assertEquals(TEST_USER_EMAIL, mockedEmailMessageDto.getReceiver());
    assertEquals(TEST_RESET_SUBJECT, mockedEmailMessageDto.getSubject());
    assertEquals(TEST_EMAIL_TEXT, mockedEmailMessageDto.getText());
    assertEquals(2, mockedResetEmailData.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void SendMailToUser_Should_Throw_IllegalArgumentException_WhenNoResetToken() {
    emailService.sendMailToUser(TEST_INVALID_RESET_EMAIL_DTO);
  }
}
