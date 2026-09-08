package com.likelion.monday.domain.notification.service;

import com.likelion.monday.domain.notification.constant.NotificationTemplate;
import com.likelion.monday.domain.notification.entity.Notification;
import com.likelion.monday.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 편지함 알림 생성을 담당한다.
 * 사연 작성 과정의 자동 알림과 관리자의 검수 결과 알림이 모두 이 서비스를 거친다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification send(Long accountId, String title, String content) {
        return notificationRepository.save(Notification.builder()
                .accountId(accountId)
                .title(title)
                .content(content)
                .build());
    }

    // 문구를 그대로 쓰는 자동 알림용이다.
    public Notification send(Long accountId, NotificationTemplate template) {
        return send(accountId, template.getTitle(), template.formatContent());
    }
}
