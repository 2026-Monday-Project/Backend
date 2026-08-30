package com.likelion.monday.domain.mygarden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "알림 편지함 목록 항목")
public record NotificationSummaryResDto(

        @Schema(description = "알림 ID", example = "1")
        Long notificationId,

        @Schema(description = "알림 제목", example = "사연이 정원에 공개되었어요")
        String title,

        @Schema(description = "읽음 여부", example = "false")
        boolean isRead,

        @Schema(description = "발송 일시")
        LocalDateTime createdAt
) {
}