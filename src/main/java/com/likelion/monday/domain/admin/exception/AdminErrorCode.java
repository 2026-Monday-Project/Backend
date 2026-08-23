package com.likelion.monday.domain.admin.exception;

import com.likelion.monday.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdminErrorCode implements ErrorCode {

    INVALID_REVIEW_STATUS(HttpStatus.BAD_REQUEST, "AD001", "검수 결과는 공개 또는 비공개만 지정할 수 있습니다."),
    STORY_ALREADY_IN_STATUS(HttpStatus.BAD_REQUEST, "AD002", "이미 해당 상태인 사연입니다."),
    NOTIFICATION_TARGET_NOT_REVIEWED(HttpStatus.BAD_REQUEST, "AD003", "검수가 끝나지 않은 사연에는 알림을 보낼 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AdminErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
