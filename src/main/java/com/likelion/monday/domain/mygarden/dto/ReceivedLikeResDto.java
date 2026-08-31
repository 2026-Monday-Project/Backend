package com.likelion.monday.domain.mygarden.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 정원 - 받은 공감 목록 항목")
public record ReceivedLikeResDto(

        @Schema(description = "공감 ID", example = "1")
        Long likeId,

        @Schema(description = "공감이 달린 사연 ID", example = "1")
        Long storyId,

        @Schema(description = "공감이 달린 사연 제목", example = "우리 집 귀염둥이에게")
        String storyTitle
) {
}