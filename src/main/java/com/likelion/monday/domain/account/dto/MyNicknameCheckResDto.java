package com.likelion.monday.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인한 사용자의 닉네임 변경 가능 여부 응답")
public record MyNicknameCheckResDto(

        @Schema(description = "true면 이 닉네임으로 변경 가능, false면 다른 계정이 이미 사용 중", example = "true")
        boolean available,

        @Schema(description = "true면 지금 내가 쓰고 있는 닉네임", example = "false")
        boolean current
) {
}
