package com.likelion.monday.domain.mygarden.exception;

import com.likelion.monday.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MyGardenErrorCode implements ErrorCode {

    STORY_NOT_FOUND(HttpStatus.NOT_FOUND, "MG001", "존재하지 않는 사연입니다."),
    STORY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "MG002", "본인이 작성한 사연만 조회할 수 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    MyGardenErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}