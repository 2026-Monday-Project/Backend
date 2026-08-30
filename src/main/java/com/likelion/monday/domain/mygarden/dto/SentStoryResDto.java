package com.likelion.monday.domain.mygarden.dto;

import com.likelion.monday.domain.story.entity.StoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "내 정원 - 보낸 사연 목록 항목")
public record SentStoryResDto(

        @Schema(description = "사연 ID", example = "1")
        Long storyId,

        @Schema(description = "사연 제목", example = "우리 집 귀염둥이에게")
        String title,

        @Schema(description = "사연 상태", example = "PENDING")
        StoryStatus status,

        @Schema(description = "제출 일시")
        LocalDateTime createdAt
) {
}