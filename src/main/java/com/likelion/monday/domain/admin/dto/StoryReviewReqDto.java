package com.likelion.monday.domain.admin.dto;

import com.likelion.monday.domain.story.entity.StoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "사연 검수 결과 반영 요청")
public record StoryReviewReqDto(

        @Schema(description = "변경할 상태. PUBLIC 또는 PRIVATE만 허용한다.", example = "PUBLIC")
        @NotNull(message = "검수 결과를 선택해 주세요.")
        StoryStatus status
) {
}
