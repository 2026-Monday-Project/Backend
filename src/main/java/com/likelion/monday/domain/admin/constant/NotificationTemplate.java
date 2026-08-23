package com.likelion.monday.domain.admin.constant;

import com.likelion.monday.domain.story.entity.StoryStatus;
import lombok.Getter;

/**
 * 검수 결과를 작성자에게 안내하는 알림 문구 템플릿.
 * 관리자가 발송 전에 내용을 고칠 수 있으므로, 여기서는 초안만 만든다.
 */
@Getter
public enum NotificationTemplate {

    STORY_PUBLIC(
            "사연이 정원에 공개되었어요",
            """
                    %s님, 보내주신 사연 <%s>이(가) 매기네정원에 공개되었어요.
                    정원 둘러보기에서 다른 관객들의 사연과 함께 확인하실 수 있습니다.
                    소중한 이야기를 나눠주셔서 고맙습니다."""),

    STORY_PRIVATE(
            "사연 검토 결과를 안내드려요",
            """
                    %s님, 보내주신 사연 <%s>은(는) 운영정책에 따라 공개되지 않았습니다.
                    사연을 보내주신 마음에 감사드리며, 문의사항은 먼데이프로젝트 인스타그램으로 연락 주세요.""");

    private final String title;
    private final String contentFormat;

    NotificationTemplate(String title, String contentFormat) {
        this.title = title;
        this.contentFormat = contentFormat;
    }

    public static NotificationTemplate from(StoryStatus status) {
        return status == StoryStatus.PUBLIC ? STORY_PUBLIC : STORY_PRIVATE;
    }

    public String formatContent(String nickname, String storyTitle) {
        return String.format(contentFormat, nickname, storyTitle);
    }
}
