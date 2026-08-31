package com.likelion.monday.domain.mygarden.dto;

import com.likelion.monday.domain.story.entity.StoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "내 정원 - 내 사연 목록 항목")
public record MyStorySummaryResDto(

        @Schema(description = "사연 ID", example = "1")
        Long storyId,

        @Schema(description = "사연 제목", example = "우리 집 귀염둥이에게")
        String title,

        @Schema(description = "반려동물 이름", example = "머고")
        String petName,

        @Schema(description = "사연 상태", example = "PENDING")
        StoryStatus status,

        @Schema(description = "받은 공감 수", example = "12")
        int likeCount,

        @Schema(description = "제출 일시")
        LocalDateTime createdAt
) {
}