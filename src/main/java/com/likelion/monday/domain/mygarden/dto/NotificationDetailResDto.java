package com.likelion.monday.domain.mygarden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "알림 상세")
public record NotificationDetailResDto(

        @Schema(description = "알림 ID", example = "1")
        Long notificationId,

        @Schema(description = "알림 제목", example = "사연이 정원에 공개되었어요")
        String title,

        @Schema(description = "알림 내용")
        String content,

        @Schema(description = "읽음 여부. 상세 조회 시점에 true로 바뀐다.", example = "true")
        boolean isRead,

        @Schema(description = "발송 일시")
        LocalDateTime createdAt
) {
}