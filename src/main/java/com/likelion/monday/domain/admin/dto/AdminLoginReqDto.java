package com.likelion.monday.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "관리자 로그인 요청")
public record AdminLoginReqDto(

        @Schema(description = "관리자 아이디", example = "admin")
        @NotBlank(message = "아이디를 입력해 주세요.")
        String username,

        @Schema(description = "관리자 비밀번호")
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password
) {
}
