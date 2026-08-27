package com.likelion.monday.domain.story.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "사연 상세 조회 응답")
public record StoryDetailResDto(

        @Schema(description = "사연 ID", example = "1")
        Long storyId,

        @Schema(description = "반려동물 이름", example = "머고")
        String petName,

        @Schema(description = "반려동물 종류", example = "고양이")
        String petType,

        @Schema(description = "반려동물 나이", example = "3")
        Integer petAge,

        @Schema(description = "사연 제목", example = "우리 집 귀염둥이에게")
        String title,

        @Schema(description = "사연 본문")
        String content,

        @Schema(description = "작성자 닉네임", example = "매기")
        String nickname,

        @Schema(description = "노출 순서대로 정렬된 사진 URL 목록")
        List<String> imageUrls,

        @Schema(description = "작성 일시")
        LocalDateTime createdAt,

        @Schema(description = "조회수", example = "128")
        int viewCount,

        @Schema(description = "공감수", example = "12")
        int likeCount
) {
}