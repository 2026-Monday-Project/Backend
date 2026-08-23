package com.likelion.monday.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "알림 발송 결과")
public record NotificationSendResDto(

        @Schema(description = "생성된 알림 ID", example = "1")
        Long notificationId,

        @Schema(description = "알림을 받은 사연 ID", example = "1")
        Long storyId,

        @Schema(description = "수신자 닉네임", example = "매기")
        String nickname,

        @Schema(description = "발송 일시")
        LocalDateTime sentAt
) {
}
