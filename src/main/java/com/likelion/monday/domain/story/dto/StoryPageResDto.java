package com.likelion.monday.domain.story.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "정원 둘러보기 사연 목록 응답")
public record StoryPageResDto(

        @Schema(description = "사연 목록")
        List<StoryCardResDto> stories,

        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int page,

        @Schema(description = "페이지당 항목 수", example = "20")
        int size,

        @Schema(description = "조건에 해당하는 전체 사연 수", example = "48")
        long totalElements,

        @Schema(description = "전체 페이지 수", example = "3")
        int totalPages
) {
}