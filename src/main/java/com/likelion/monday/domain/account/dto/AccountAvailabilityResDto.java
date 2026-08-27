package com.likelion.monday.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "중복 확인 응답")
public record AccountAvailabilityResDto(

        @Schema(description = "true면 사용 가능, false면 이미 사용 중", example = "true")
        boolean available
) {
}
