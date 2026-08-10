package com.likelion.monday.domain.account.exception;

import com.likelion.monday.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AccountErrorCode implements ErrorCode {

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "존재하지 않는 계정입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AccountErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
