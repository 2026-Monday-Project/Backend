package com.likelion.monday.domain.story.dto;

import com.likelion.monday.domain.story.entity.StoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "사연 작성·수정 응답")
public record StoryWriteResDto(

        @Schema(description = "사연 ID", example = "1")
        Long storyId,

        @Schema(description = "사연에 표시되는 닉네임", example = "매기")
        String nickname,

        @Schema(description = "사연 공개 상태 (제출 직후에는 PENDING)", example = "PENDING")
        StoryStatus status,

        @Schema(description = "노출 순서대로 정렬된 사진 URL 목록")
        List<String> imageUrls
) {
}
