package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.NotificationDto;
import io.realmarket.propeler.model.Notification;

import java.util.Date;

public class NotificationUtils {
  public static final Long TEST_NOTIFICATION_ID = 1L;
  public static final String TEST_NOTIFICATION_TITLE = "TEST_NOTIFICATION_TITLE";
  public static final String TEST_NOTIFICATION_CONTENT = "TEST_NOTIFICATION_CONTENT";
  public static final Date TEST_NOTIFICATION_DATE = new Date();

  public static Notification mockNotificationUnseen() {
    return Notification.builder()
        .id(TEST_NOTIFICATION_ID)
        .title(TEST_NOTIFICATION_TITLE)
        .content(TEST_NOTIFICATION_CONTENT)
        .sender(AuthUtils.TEST_AUTH_ADMIN)
        .recipient(AuthUtils.TEST_AUTH_ENTREPRENEUR)
        .date(TEST_NOTIFICATION_DATE)
        .active(true)
        .seen(false)
        .build();
  }

  public static Notification mockNotificationSeen() {
    return Notification.builder()
        .id(TEST_NOTIFICATION_ID)
        .title(TEST_NOTIFICATION_TITLE)
        .content(TEST_NOTIFICATION_CONTENT)
        .sender(AuthUtils.TEST_AUTH_ADMIN)
        .recipient(AuthUtils.TEST_AUTH_ENTREPRENEUR)
        .date(TEST_NOTIFICATION_DATE)
        .active(true)
        .seen(true)
        .build();
  }

  public static NotificationDto mockNotificationDtoSeen() {
    Notification notification = mockNotificationSeen();
    return new NotificationDto(notification);
  }

  public static NotificationDto mockNotificationDtoUnseen() {
    Notification notification = mockNotificationUnseen();
    return new NotificationDto(notification);
  }
}
