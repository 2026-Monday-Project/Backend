package com.likelion.monday.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "관리자 로그인 응답")
public record AdminLoginResDto(

        @Schema(description = "발급된 액세스 토큰. 이후 요청의 Authorization 헤더에 Bearer로 담아 보낸다.")
        String accessToken,

        @Schema(description = "토큰 타입", example = "Bearer")
        String tokenType,

        @Schema(description = "토큰 만료까지 남은 시간(초)", example = "43200")
        long expiresIn
) {
}
