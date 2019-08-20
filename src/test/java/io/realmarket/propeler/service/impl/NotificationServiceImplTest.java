package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.NotificationDto;
import io.realmarket.propeler.model.Notification;
import io.realmarket.propeler.repository.NotificationRepository;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.NotificationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.util.NotificationUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NotificationServiceImpl.class)
public class NotificationServiceImplTest {

  @Mock private NotificationRepository notificationRepository;

  @InjectMocks private NotificationServiceImpl notificationServiceImpl;

  @Test
  public void GetNumberOfUnseenNotifications_Should_Return_UnseenNotificationNumber() {
    AuthUtils.mockRequestAndContext();

    when(notificationRepository.countNotificationByRecipientAndSeenAndActive(
            TEST_AUTH, false, true))
        .thenReturn(1L);

    Long retVal = notificationServiceImpl.getNumberUnseenNotifications();

    assertEquals(Long.valueOf(1), retVal);
  }

  @Test
  public void GetAllNotification_Should_Return_AllUserNotifications() {
    AuthUtils.mockRequestAndContext();

    Pageable pageable = Mockito.mock(Pageable.class);
    Notification notification = mockNotificationSeen();
    NotificationDto notificationDto = mockNotificationDtoSeen();
    Page<NotificationDto> notificationsDto =
        new PageImpl<>(Collections.singletonList(notificationDto));
    Page<Notification> notifications = new PageImpl<>(Collections.singletonList(notification));

    when(notificationRepository.findAllByRecipientAndActiveOrderByDateDesc(
            pageable, TEST_AUTH, true))
        .thenReturn(notifications);

    Page<NotificationDto> retVal = notificationServiceImpl.getAllNotifications(pageable, null);

    verify(notificationRepository, times(1))
        .findAllByRecipientAndActiveOrderByDateDesc(pageable, TEST_AUTH, true);
    assertEquals(retVal, notificationsDto);
  }

  @Test
  public void GetAllNotification_Should_Return_AllUserUnseensNotifications() {
    AuthUtils.mockRequestAndContext();

    Pageable pageable = Mockito.mock(Pageable.class);
    Notification notification = mockNotificationUnseen();
    NotificationDto notificationDto = mockNotificationDtoUnseen();
    Page<NotificationDto> notificationsDto =
        new PageImpl<>(Collections.singletonList(notificationDto));
    Page<Notification> notifications = new PageImpl<>(Collections.singletonList(notification));

    when(notificationRepository.findAllByRecipientAndSeenAndActiveOrderByDateDesc(
            pageable, TEST_AUTH, false, true))
        .thenReturn(notifications);

    Page<NotificationDto> retVal = notificationServiceImpl.getAllNotifications(pageable, false);

    verify(notificationRepository, times(1))
        .findAllByRecipientAndSeenAndActiveOrderByDateDesc(pageable, TEST_AUTH, false, true);
    assertEquals(retVal, notificationsDto);
  }

  @Test
  public void GetAllNotification_Should_Return_AllUserSeenNotifications() {
    AuthUtils.mockRequestAndContext();

    Pageable pageable = Mockito.mock(Pageable.class);
    NotificationDto notificationDto = mockNotificationDtoSeen();
    Notification notification = mockNotificationSeen();
    Page<NotificationDto> notificationsDto =
        new PageImpl<>(Collections.singletonList(notificationDto));
    Page<Notification> notifications = new PageImpl<>(Collections.singletonList(notification));

    when(notificationRepository.findAllByRecipientAndSeenAndActiveOrderByDateDesc(
            pageable, TEST_AUTH, true, true))
        .thenReturn(notifications);

    Page<NotificationDto> retVal = notificationServiceImpl.getAllNotifications(pageable, true);

    verify(notificationRepository, times(1))
        .findAllByRecipientAndSeenAndActiveOrderByDateDesc(pageable, TEST_AUTH, true, true);
    assertEquals(retVal, notificationsDto);
  }

  @Test
  public void ChangeNotificationStatus_Should_ChangeNotificationStatus() {
    when(notificationRepository.findById(NotificationUtils.TEST_NOTIFICATION_ID))
        .thenReturn(Optional.ofNullable(mockNotificationSeen()));
    AuthUtils.mockRequestAndContext();
    when(notificationRepository.save(any())).thenReturn(mockNotificationUnseen());

    notificationServiceImpl.changeNotificationSeenStatus(NotificationUtils.TEST_NOTIFICATION_ID);

    verify(notificationRepository, times(1)).save(any());
  }

  @Test(expected = EntityNotFoundException.class)
  public void ChangeNotificationStatus_Should_Throw_EntityNotFoundException() {
    when(notificationRepository.findById(NotificationUtils.TEST_NOTIFICATION_ID))
        .thenReturn(Optional.empty());

    notificationServiceImpl.deleteNotification(NotificationUtils.TEST_NOTIFICATION_ID);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void ChangeNotificationStatus_Should_Throw_ForbiddenOperationException() {
    when(notificationRepository.findById(NotificationUtils.TEST_NOTIFICATION_ID))
        .thenReturn(Optional.ofNullable(mockNotificationSeen()));
    AuthUtils.mockRequestAndContextAdmin();

    notificationServiceImpl.changeNotificationSeenStatus(NotificationUtils.TEST_NOTIFICATION_ID);
  }

  @Test
  public void DeleteNotification_Should_DeleteNotification() {
    when(notificationRepository.findById(NotificationUtils.TEST_NOTIFICATION_ID))
        .thenReturn(Optional.ofNullable(mockNotificationSeen()));
    AuthUtils.mockRequestAndContext();
    when(notificationRepository.save(any())).thenReturn(mockNotificationSeen());

    notificationServiceImpl.deleteNotification(NotificationUtils.TEST_NOTIFICATION_ID);

    verify(notificationRepository, times(1)).save(any());
  }

  @Test(expected = EntityNotFoundException.class)
  public void DeleteNotification_Should_Throw_EntityNotFoundException() {
    when(notificationRepository.findById(NotificationUtils.TEST_NOTIFICATION_ID))
        .thenReturn(Optional.empty());

    notificationServiceImpl.deleteNotification(NotificationUtils.TEST_NOTIFICATION_ID);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void DeleteNotification_Should_Throw_ForbiddenOperationException() {
    when(notificationRepository.findById(NotificationUtils.TEST_NOTIFICATION_ID))
        .thenReturn(Optional.ofNullable(mockNotificationSeen()));
    AuthUtils.mockRequestAndContextAdmin();

    notificationServiceImpl.deleteNotification(NotificationUtils.TEST_NOTIFICATION_ID);
  }
}
