package com.likelion.monday.domain.performance.exception;

import com.likelion.monday.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PerformanceErrorCode implements ErrorCode {

    PERFORMANCE_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "등록된 먼데이 원송 콘텐츠가 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    PerformanceErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}