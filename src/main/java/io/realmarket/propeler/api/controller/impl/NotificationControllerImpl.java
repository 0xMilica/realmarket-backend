package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.NotificationController;
import io.realmarket.propeler.api.dto.NotificationDto;
import io.realmarket.propeler.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationControllerImpl implements NotificationController {

  private final NotificationService notificationService;

  @Autowired
  public NotificationControllerImpl(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping
  @PreAuthorize(
      "hasAnyAuthority('ROLE_INDIVIDUAL_INVESTOR', 'ROLE_CORPORATE_INVESTOR', 'ROLE_ENTREPRENEUR','ROLE_ADMIN')")
  public ResponseEntity<Page<NotificationDto>> getAllNotifications(
      Pageable pageable, @RequestParam(value = "filter", required = false) Boolean filter) {
    return ResponseEntity.ok(notificationService.getAllNotifications(pageable, filter));
  }

  @GetMapping(value = "/numberOfUnseen")
  @PreAuthorize(
      "hasAnyAuthority('ROLE_INDIVIDUAL_INVESTOR', 'ROLE_CORPORATE_INVESTOR', 'ROLE_ENTREPRENEUR','ROLE_ADMIN')")
  public ResponseEntity<Long> getNumberUnseenNotifications() {
    return ResponseEntity.ok(notificationService.getNumberUnseenNotifications());
  }

  @PatchMapping(value = "/changeStatus/{id}")
  @PreAuthorize(
      "hasAnyAuthority('ROLE_INDIVIDUAL_INVESTOR', 'ROLE_CORPORATE_INVESTOR', 'ROLE_ENTREPRENEUR','ROLE_ADMIN')")
  public ResponseEntity<Void> changeNotificationSeenStatus(@PathVariable Long id) {
    notificationService.changeNotificationSeenStatus(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "{id}")
  @PreAuthorize(
      "hasAnyAuthority('ROLE_INDIVIDUAL_INVESTOR', 'ROLE_CORPORATE_INVESTOR', 'ROLE_ENTREPRENEUR','ROLE_ADMIN')")
  public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
    notificationService.deleteNotification(id);
    return ResponseEntity.noContent().build();
  }
}
