package com.likelion.monday.domain.mygarden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "내 정원 - 공감한 사연 목록 항목")
public record LikedStoryResDto(

        @Schema(description = "사연 ID", example = "1")
        Long storyId,

        @Schema(description = "사연 제목", example = "우리 집 귀염둥이에게")
        String storyTitle,

        @Schema(description = "공감한 일시")
        LocalDateTime likedAt
) {
}