package com.likelion.monday.domain.notification.repository;

import com.likelion.monday.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop2ByAccountIdOrderByCreatedAtDesc(Long accountId);

    List<Notification> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);
}