package com.likelion.monday.domain.notification.entity;

import com.likelion.monday.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private boolean isRead;

    // 관리자가 발송한 알림은 항상 읽지 않은 상태로 생성된다.
    @Builder
    private Notification(Long accountId, String title, String content) {
        this.accountId = accountId;
        this.title = title;
        this.content = content;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
