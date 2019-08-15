package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.NotificationDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
  void sendMessage(Auth recipient, NotificationType type, String rejectionMessage);

  Long getNumberUnseenNotifications();

  Page<NotificationDto> getAllNotifications(Pageable pageable, Boolean filter);

  void changeNotificationSeenStatus(Long id);
}
