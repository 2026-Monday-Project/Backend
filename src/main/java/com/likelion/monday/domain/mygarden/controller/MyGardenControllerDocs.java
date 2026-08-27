package com.likelion.monday.domain.mygarden.controller;

import com.likelion.monday.domain.mygarden.dto.MyStorySummaryResDto;
import com.likelion.monday.domain.story.entity.StoryStatus;
import com.likelion.monday.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "MyGarden", description = "내 정원 API")
@RequestMapping("/my-garden")
public interface MyGardenControllerDocs {

    @Operation(
            summary = "내 사연 미리보기 조회",
            description = "마이페이지 요약용으로 최신 사연 2건만 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<List<MyStorySummaryResDto>> getMyStoriesPreview(@Parameter(hidden = true) Long accountId);

    @Operation(
            summary = "내 사연 전체/필터 조회",
            description = "status를 생략하면 전체(ALL), 지정하면 검토중/공개/비공개로 필터링해 최신순으로 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<List<MyStorySummaryResDto>> getMyStories(@Parameter(hidden = true) Long accountId, StoryStatus status);
}