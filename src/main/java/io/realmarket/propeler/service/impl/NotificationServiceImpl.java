package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.NotificationContentDto;
import io.realmarket.propeler.api.dto.NotificationDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Notification;
import io.realmarket.propeler.model.enums.NotificationType;
import io.realmarket.propeler.repository.NotificationRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.NotificationService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.USER_IS_NOT_RECIPIENT_OF_NOTIFICATION;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

  private static final String NOTIFICATIONS_BROKER = "/notifications";
  private final NotificationRepository notificationRepository;
  private final SimpMessagingTemplate template;

  @Autowired
  public NotificationServiceImpl(
      NotificationRepository notificationRepository, SimpMessagingTemplate template) {
    this.notificationRepository = notificationRepository;
    this.template = template;
  }

  @Override
  public void sendMessage(Auth recipient, NotificationType type, String rejectionMessage) {
    Auth sender = AuthenticationUtil.getAuthentication().getAuth();
    String content;
    NotificationContentDto notificationContent = generateNotificationContent(recipient, type);

    if (rejectionMessage != null && !rejectionMessage.isEmpty()) {
      content = notificationContent.getText().concat(rejectionMessage);
    } else {
      content = notificationContent.getText();
    }

    Notification notification =
        Notification.builder()
            .content(content)
            .date(new Date())
            .sender(sender)
            .recipient(recipient)
            .title(notificationContent.getTitle())
            .build();
    notificationRepository.save(notification);
    template.convertAndSendToUser(
        recipient.getId().toString(), NOTIFICATIONS_BROKER, new NotificationDto(notification));
  }

  @Override
  public Long getNumberUnseenNotifications() {
    Auth recipient = AuthenticationUtil.getAuthentication().getAuth();
    return notificationRepository.countNotificationByRecipientAndSeenAndActive(
        recipient, false, true);
  }

  @Override
  public Page<NotificationDto> getAllNotifications(Pageable pageable, Boolean filter) {
    Auth recipient = AuthenticationUtil.getAuthentication().getAuth();
    if (filter == null)
      return notificationRepository
          .findAllByRecipientAndActiveOrderByDateDesc(pageable, recipient, true)
          .map(NotificationDto::new);
    else
      return notificationRepository
          .findAllByRecipientAndSeenAndActiveOrderByDateDesc(pageable, recipient, filter, true)
          .map(NotificationDto::new);
  }

  @Override
  public void changeNotificationSeenStatus(Long id) {
    Notification notification =
        notificationRepository
            .findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOTIFICATION_DOES_NOT_EXIST));
    Auth currentUser = AuthenticationUtil.getAuthentication().getAuth();
    if (!notification.getRecipient().getId().equals(currentUser.getId())) {
      throw new ForbiddenOperationException(USER_IS_NOT_RECIPIENT_OF_NOTIFICATION);
    }
    notification.setSeen(!notification.getSeen());
    notificationRepository.save(notification);
  }

  @Override
  public void deleteNotification(Long id) {
    Notification notification =
        notificationRepository
            .findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOTIFICATION_DOES_NOT_EXIST));
    Auth currentUser = AuthenticationUtil.getAuthentication().getAuth();
    if (!notification.getRecipient().getId().equals(currentUser.getId())) {
      throw new ForbiddenOperationException(USER_IS_NOT_RECIPIENT_OF_NOTIFICATION);
    }
    notification.setActive(false);
    notificationRepository.save(notification);
  }

  private NotificationContentDto generateNotificationContent(
      Auth recipient, NotificationType type) {
    String title = "";
    String templateText =
        String.format(
            "Dear %s %s",
            recipient.getPerson().getFirstName(), recipient.getPerson().getLastName());
    switch (type) {
      case ACCEPT_CAMPAIGN:
        title = "Propeler - Campaign accepted";
        templateText = templateText.concat(", your campaign has been accepted. ");
        break;
      case REJECT_CAMPAIGN:
        title = "Propeler - Campaign rejected";
        templateText = templateText.concat(", your campaign has been rejected. ");
        break;
      case KYC_APPROVAL:
        title = "Propeler - KYC accepted";
        templateText =
            templateText.concat(
                ", we have received your KYC application. After careful review we are glad to inform you that Your KYC application has been approved. From now on, you are able to raise funds on Propeler platform. ");
        break;
      case KYC_REJECTION:
        title = "Propeler - KYC rejection";
        templateText =
            templateText.concat(
                ", we have received your KYC application. After careful review we are sorry to inform you that Your KYC application has been rejected. ");
        break;
      default:
        break;
    }

    return NotificationContentDto.builder().title(title).text(templateText).build();
  }
}
