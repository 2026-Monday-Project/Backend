package com.likelion.monday.domain.mygarden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

@Schema(description = "공통 페이지네이션 응답")
public record PageResDto<T>(

        @Schema(description = "목록")
        List<T> content,

        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int page,

        @Schema(description = "페이지당 항목 수", example = "20")
        int size,

        @Schema(description = "전체 항목 수", example = "48")
        long totalElements,

        @Schema(description = "전체 페이지 수", example = "3")
        int totalPages
) {
    public static <T> PageResDto<T> from(Page<T> page) {
        return new PageResDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}