package com.likelion.monday.domain.mygarden.dto;

import com.likelion.monday.domain.story.entity.StoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "내 정원 - 내 사연 상세")
public record MyStoryDetailResDto(

        @Schema(description = "사연 ID", example = "1")
        Long storyId,

        @Schema(description = "사연 제목", example = "우리 집 귀염둥이에게")
        String title,

        @Schema(description = "사연 본문")
        String content,

        @Schema(description = "반려동물 이름", example = "머고")
        String petName,

        @Schema(description = "반려동물 종류", example = "강아지")
        String petType,

        @Schema(description = "반려동물 나이", example = "3")
        Integer petAge,

        @Schema(description = "사연 상태", example = "PENDING")
        StoryStatus status,

        @Schema(description = "조회 수", example = "128")
        int viewCount,

        @Schema(description = "공감 수", example = "12")
        int likeCount,

        @Schema(description = "[선택] 공연 중 소개·낭독 동의 현황")
        boolean introduceConsent,

        @Schema(description = "[선택] SNS·홍보물 활용 동의 현황")
        boolean snsConsent,

        @Schema(description = "노출 순서대로 정렬된 사진 URL 목록")
        List<String> imageUrls,

        @Schema(description = "제출 일시")
        LocalDateTime createdAt,

        @Schema(description = "최종 수정 일시")
        LocalDateTime updatedAt
) {
}