package com.likelion.monday.domain.performance.controller;

import com.likelion.monday.domain.performance.dto.PerformanceContentResDto;
import com.likelion.monday.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Performance", description = "먼데이 원송 관련 API")
@RequestMapping("/performance")
public interface PerformanceContentControllerDocs {

    @Operation(
            summary = "먼데이 원송 공개 상태 조회",
            description = """
                    먼데이 원송(One Song) 콘텐츠의 공개 상태를 조회한다.
                    저장된 공개 예정 시각(contentOpenAt)과 현재 시각을 비교해 COMING_SOON/OPEN 상태를 계산해서 응답한다.
                    등록된 콘텐츠가 없으면 404로 처리한다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "등록된 먼데이 원송 콘텐츠가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<PerformanceContentResDto> getPerformanceContent();
}