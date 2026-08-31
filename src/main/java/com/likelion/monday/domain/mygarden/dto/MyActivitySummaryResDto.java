package com.likelion.monday.domain.mygarden.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 정원 홈 - 활동 요약")
public record MyActivitySummaryResDto(

        @Schema(description = "내가 보낸 사연 수", example = "3")
        long sentStoryCount,

        @Schema(description = "내가 쓴 사연들이 받은 공감 수", example = "24")
        long receivedLikeCount,

        @Schema(description = "내가 남긴 공감 수", example = "10")
        long likedStoryCount
) {
}