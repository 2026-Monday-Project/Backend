package com.likelion.monday.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 문구 초안")
public record NotificationDraftResDto(

        @Schema(description = "자동 완성된 알림 제목", example = "사연이 정원에 공개되었어요")
        String title,

        @Schema(description = "자동 완성된 알림 내용")
        String content
) {
}
