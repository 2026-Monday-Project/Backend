package com.likelion.monday.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "관리자 메인 현황")
public record AdminStoryCountResDto(

        @Schema(description = "검토를 기다리는 사연 수", example = "12")
        long pendingCount,

        @Schema(description = "공개 처리된 사연 수", example = "34")
        long publicCount,

        @Schema(description = "비공개 처리된 사연 수", example = "2")
        long privateCount,

        @Schema(description = "전체 사연 수", example = "48")
        long totalCount
) {
}
