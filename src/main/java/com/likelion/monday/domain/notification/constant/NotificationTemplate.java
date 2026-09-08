package com.likelion.monday.domain.notification.constant;

import com.likelion.monday.domain.story.entity.StoryStatus;
import lombok.Getter;

/**
 * 편지함에 쌓이는 알림 문구 템플릿.
 * WELCOME과 STORY_SUBMITTED는 사연 작성 과정에서 자동으로 발송된다.
 * STORY_PUBLIC과 STORY_PRIVATE는 관리자가 검수 후 내용을 확인·수정해 발송하는 초안이다.
 */
@Getter
public enum NotificationTemplate {

    WELCOME(
            "정원에 오신 걸 환영합니다.",
            "따뜻한 이야기를 함께 나눠보세요."),

    STORY_SUBMITTED(
            "당신의 이야기가 정원에 도착했어요.",
            "운영팀 검수 후 공개 여부와 상태를 내 정원에서 확인하실 수 있어요."),

    STORY_PUBLIC(
            "당신의 이야기가 정원에 공개되었어요",
            """
                    운영팀 검수 후 %s님의 사연이 정원에 공개되었어요. 다른 사용자가 당신의 사연을 읽고 공감할 수 있어요."""),

    STORY_PRIVATE(
            "당신의 이야기가 숨겨졌어요",
            """
                    %s님, 운영팀 검수 결과, 사연 방침에 어긋나 공개하지 못했어요. 사연을 수정하고 다시 제출해 보세요.""");

    private final String title;
    private final String content;

    NotificationTemplate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static NotificationTemplate from(StoryStatus status) {
        return status == StoryStatus.PUBLIC ? STORY_PUBLIC : STORY_PRIVATE;
    }

    // 치환할 값이 없는 문구는 그대로 돌려준다.
    public String formatContent(Object... args) {
        return args.length == 0 ? content : String.format(content, args);
    }
}
