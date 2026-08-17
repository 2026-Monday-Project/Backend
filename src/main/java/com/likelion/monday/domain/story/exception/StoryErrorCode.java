package com.likelion.monday.domain.story.exception;

import com.likelion.monday.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StoryErrorCode implements ErrorCode {

    STORY_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "존재하지 않는 사연입니다."),
    STORY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "S002", "본인이 작성한 사연만 수정할 수 있습니다."),
    STORY_NOT_EDITABLE(HttpStatus.BAD_REQUEST, "S003", "검토중인 사연만 수정할 수 있습니다."),
    STORY_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "S004", "해당 사연에 존재하지 않는 사진입니다."),
    IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "S005", "사진은 최대 5장까지 첨부할 수 있습니다."),
    INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "S006", "이미지 파일만 첨부할 수 있습니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "S007", "이미 사용 중인 닉네임입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    StoryErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
