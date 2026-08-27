package com.likelion.monday.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "이메일 로그인 요청")
public record AccountLoginReqDto(

        @Schema(description = "사연 제출에 사용한 이메일", example = "monday@example.com")
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(max = 100, message = "이메일은 100자 이내로 입력해 주세요.")
        String email
) {
}
