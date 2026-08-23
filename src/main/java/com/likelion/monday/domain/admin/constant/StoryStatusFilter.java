package com.likelion.monday.domain.admin.constant;

import com.likelion.monday.domain.story.entity.StoryStatus;

/**
 * 관리자 사연 목록의 상태 필터.
 * StoryStatus에는 "전체"라는 값이 없으므로 목록 조회 전용으로 ALL을 더해 구분한다.
 */
public enum StoryStatusFilter {

    PENDING(StoryStatus.PENDING),
    PUBLIC(StoryStatus.PUBLIC),
    PRIVATE(StoryStatus.PRIVATE),
    ALL(null);

    private final StoryStatus storyStatus;

    StoryStatusFilter(StoryStatus storyStatus) {
        this.storyStatus = storyStatus;
    }

    // ALL이면 상태 조건 없이 조회해야 하므로 null을 돌려준다.
    public StoryStatus toStoryStatus() {
        return storyStatus;
    }
}
