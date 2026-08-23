package com.likelion.monday.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "알림 발송 요청")
public record NotificationSendReqDto(

        @Schema(description = "알림 제목", example = "사연이 정원에 공개되었어요")
        @NotBlank(message = "알림 제목을 입력해 주세요.")
        @Size(max = 100, message = "알림 제목은 100자 이내로 입력해 주세요.")
        String title,

        @Schema(description = "알림 내용")
        @NotBlank(message = "알림 내용을 입력해 주세요.")
        @Size(max = 500, message = "알림 내용은 500자 이내로 입력해 주세요.")
        String content
) {
}
