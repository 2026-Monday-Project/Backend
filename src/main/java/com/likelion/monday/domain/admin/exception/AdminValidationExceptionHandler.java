package com.likelion.monday.domain.admin.exception;

import com.likelion.monday.global.exception.CommonErrorCode;
import com.likelion.monday.global.exception.ErrorCode;
import com.likelion.monday.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 관리자 요청의 입력값 오류를 화면에 그대로 보여줄 수 있는 메시지로 변환한다.
 * GlobalExceptionHandler는 Exception까지 잡아 500으로 내리므로 그보다 먼저 동작하도록 우선순위를 올린다.
 * 추후 공통 예외 처리에 동일 핸들러가 추가되면 이 클래스는 삭제한다.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.likelion.monday.domain.admin")
public class AdminValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null
                ? CommonErrorCode.INVALID_INPUT_VALUE.getMessage()
                : fieldError.getDefaultMessage();
        log.warn("관리자 요청 검증 실패: {}", message);

        return toResponse(new InputErrorCode(CommonErrorCode.INVALID_INPUT_VALUE, message));
    }

    // status=WRONG처럼 정의되지 않은 필터 값이 들어온 경우다.
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        log.warn("관리자 요청 파라미터 형식 오류: name={}, value={}", e.getName(), e.getValue());

        return toResponse(new InputErrorCode(
                CommonErrorCode.INVALID_TYPE_VALUE,
                CommonErrorCode.INVALID_TYPE_VALUE.getMessage()));
    }

    private ResponseEntity<ApiResponse<Void>> toResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode));
    }

    // 검증 실패는 항목마다 안내 문구가 달라 공통 코드에 담을 수 없으므로 메시지만 갈아끼워 내려준다.
    private record InputErrorCode(CommonErrorCode commonErrorCode, String message) implements ErrorCode {

        @Override
        public HttpStatus getStatus() {
            return commonErrorCode.getStatus();
        }

        @Override
        public String getCode() {
            return commonErrorCode.getCode();
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
