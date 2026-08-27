package com.likelion.monday.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "닉네임 중복 확인 요청")
public record NicknameCheckReqDto(

        @Schema(description = "확인할 닉네임", example = "매기")
        @NotBlank(message = "닉네임을 입력해 주세요.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$", message = "닉네임은 한글, 영문, 숫자로 10자 이내로 입력해 주세요.")
        String nickname
) {
}
