package com.likelion.monday.domain.notification.repository;

import com.likelion.monday.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}