package com.likelion.monday.domain.story.constant;

import org.springframework.data.domain.Sort;

/**
 * 사연 목록 정렬 기준.
 * 쿼리 파라미터 이름과 실제 정렬 대상 필드를 분리해서, 엔티티 필드명이 바뀌어도
 * API 파라미터(sort=LATEST 등)는 그대로 유지할 수 있게 한다.
 */
public enum StorySort {

    LATEST("createdAt"),
    VIEWS("viewCount"),
    LIKES("likeCount");

    private final String field;

    StorySort(String field) {
        this.field = field;
    }

    public Sort toSort() {
        return Sort.by(Sort.Direction.DESC, field)
                .and(Sort.by(Sort.Direction.DESC, "id"));
    }
}