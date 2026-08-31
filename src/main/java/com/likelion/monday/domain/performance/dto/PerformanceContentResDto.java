package com.likelion.monday.domain.performance.dto;

import com.likelion.monday.domain.performance.entity.PerformanceContentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "먼데이 원송 공개 상태 응답")
public record PerformanceContentResDto(

        @Schema(description = "공개 상태 (현재 시각 기준으로 계산됨)", example = "COMING_SOON")
        PerformanceContentStatus contentStatus,

        @Schema(description = "공개 예정 시각", example = "2026-09-27T00:00:00")
        LocalDateTime contentOpenAt
) {
}