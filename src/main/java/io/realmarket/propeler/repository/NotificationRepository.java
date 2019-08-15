package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  Long countNotificationByRecipientAndSeen(Auth recipient, boolean seen);

  Page<Notification> findAllByRecipientAndSeenOrderByDateDesc(
      Pageable pageable, Auth recipient, boolean seen);

  Page<Notification> findAllByRecipientOrderByDateDesc(Pageable pageable, Auth recipient);
}
