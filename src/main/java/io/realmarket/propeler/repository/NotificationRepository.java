package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  Long countNotificationByRecipientAndSeenAndActive(Auth recipient, boolean seen, boolean active);

  Page<Notification> findAllByRecipientAndSeenAndActiveOrderByDateDesc(
      Pageable pageable, Auth recipient, boolean seen, boolean active);

  Page<Notification> findAllByRecipientAndActiveOrderByDateDesc(
      Pageable pageable, Auth recipient, boolean active);
}
