package com.likelion.monday.domain.story.exception;

import com.likelion.monday.global.exception.CommonErrorCode;
import com.likelion.monday.global.exception.ErrorCode;
import com.likelion.monday.global.response.ApiResponse;
import com.likelion.monday.global.storage.StorageErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 사연 작성·수정 요청의 입력값 검증 실패를 프론트가 그대로 보여줄 수 있는 메시지로 변환한다.
 * GlobalExceptionHandler는 Exception까지 잡아 500으로 내리므로, 그보다 먼저 동작하도록 우선순위를 올린다.
 * 추후 공통 예외 처리에 동일 핸들러가 추가되면 이 클래스는 삭제한다.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.likelion.monday.domain.story")
public class StoryValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null
                ? CommonErrorCode.INVALID_INPUT_VALUE.getMessage()
                : fieldError.getDefaultMessage();
        log.warn("사연 요청 검증 실패: {}", message);

        return toResponse(new InputErrorCode(message));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("첨부 파일 용량 초과", e);

        return toResponse(StorageErrorCode.FILE_SIZE_EXCEEDED);
    }

    private ResponseEntity<ApiResponse<Void>> toResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode));
    }

    // 검증 실패는 항목마다 안내 문구가 달라 공통 코드에 담을 수 없으므로 메시지만 갈아끼워 내려준다.
    private record InputErrorCode(String message) implements ErrorCode {

        @Override
        public HttpStatus getStatus() {
            return CommonErrorCode.INVALID_INPUT_VALUE.getStatus();
        }

        @Override
        public String getCode() {
            return CommonErrorCode.INVALID_INPUT_VALUE.getCode();
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
