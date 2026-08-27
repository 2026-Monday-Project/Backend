package com.likelion.monday.domain.story.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "정원 둘러보기 사연 목록 항목")
public record StoryCardResDto(

        @Schema(description = "사연 ID", example = "1")
        Long storyId,

        @Schema(description = "대표 사진 URL. 첨부 사진이 없으면 null")
        String thumbnailUrl,

        @Schema(description = "반려동물 이름", example = "머고")
        String petName,

        @Schema(description = "사연 제목", example = "우리 집 귀염둥이에게")
        String title,

        @Schema(description = "작성자 닉네임", example = "매기")
        String nickname,

        @Schema(description = "작성 일시")
        LocalDateTime createdAt,

        @Schema(description = "조회수", example = "128")
        int viewCount,

        @Schema(description = "공감수", example = "12")
        int likeCount
) {
}