package com.likelion.monday.domain.mygarden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "내 정원 - 공감한 사연 목록 항목")
public record LikedStoryResDto(

        @Schema(description = "사연 ID", example = "1")
        Long storyId,

        @Schema(description = "대표 사진 URL. 첨부 사진이 없으면 null")
        String thumbnailUrl,

        @Schema(description = "사연 제목", example = "산책 한마디에 대소동")
        String storyTitle,

        @Schema(description = "반려동물 이름", example = "루이")
        String petName,

        @Schema(description = "반려동물 종류", example = "골든리트리버")
        String petType,

        @Schema(description = "반려동물 나이", example = "8")
        Integer petAge,

        @Schema(description = "조회 수", example = "1")
        int viewCount,

        @Schema(description = "공감 수", example = "1")
        int likeCount,

        @Schema(description = "사연 작성 일시")
        LocalDateTime createdAt
) {
}