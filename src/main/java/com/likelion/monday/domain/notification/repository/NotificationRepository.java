package com.likelion.monday.domain.notification.repository;

import com.likelion.monday.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 알림 편지함 미리보기 (최신 2건). createdAt이 같을 경우 id로 순서를 보장한다.
    List<Notification> findTop2ByAccountIdOrderByCreatedAtDescIdDesc(Long accountId);

    // 알림 편지함 전체 조회 (페이지네이션)
    Page<Notification> findAllByAccountId(Long accountId, Pageable pageable);
}