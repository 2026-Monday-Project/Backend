package com.likelion.monday.domain.account.exception;

import com.likelion.monday.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AccountErrorCode implements ErrorCode {

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "존재하지 않는 계정입니다."),
    EMAIL_NOT_SUBMITTED(HttpStatus.NOT_FOUND, "A002", "사연을 제출한 적 없는 이메일입니다."),
    NICKNAME_ALREADY_USED(HttpStatus.CONFLICT, "A003", "이미 사용 중인 닉네임입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AccountErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
