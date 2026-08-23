package com.likelion.monday.global.jwt;

import com.likelion.monday.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JwtErrorCode implements ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "J001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "J002", "로그인이 만료되었습니다. 다시 로그인해 주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    JwtErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
