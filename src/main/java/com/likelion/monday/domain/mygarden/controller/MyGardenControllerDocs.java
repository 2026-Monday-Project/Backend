package com.likelion.monday.domain.mygarden.controller;

import com.likelion.monday.domain.mygarden.dto.*;
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

    @Operation(
            summary = "내 정원 홈 - 활동 요약 조회",
            description = "보낸 사연 수, 공감받은 수, 공감한 사연 수를 개수로만 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<MyActivitySummaryResDto> getActivitySummary(@Parameter(hidden = true) Long accountId);

    @Operation(
            summary = "보낸 사연 조회",
            description = "내가 작성한 사연 전체를 최신순으로 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<List<SentStoryResDto>> getSentStories(@Parameter(hidden = true) Long accountId);

    @Operation(
            summary = "받은 공감 조회",
            description = "내가 쓴 사연들에 달린 공감을 최신순으로 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<List<ReceivedLikeResDto>> getReceivedLikes(@Parameter(hidden = true) Long accountId);

    @Operation(
            summary = "공감한 사연 조회",
            description = "내가 다른 사연에 남긴 공감을 최신순으로 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<List<LikedStoryResDto>> getLikedStories(@Parameter(hidden = true) Long accountId);

    @Operation(
            summary = "내 사연 상세 조회",
            description = "공개 상태와 선택 동의(소개·낭독, SNS 활용) 현황을 함께 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<MyStoryDetailResDto> getMyStoryDetail(@Parameter(hidden = true) Long accountId, Long storyId);

    @Operation(
            summary = "내 사연 삭제",
            description = "본인이 작성한 사연만 삭제할 수 있다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<Void> deleteMyStory(@Parameter(hidden = true) Long accountId, Long storyId);

    @Operation(
            summary = "알림 편지함 미리보기 조회",
            description = "마이페이지 요약용으로 최신 알림 2건만 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<List<NotificationSummaryResDto>> getNotificationsPreview(@Parameter(hidden = true) Long accountId);

    @Operation(
            summary = "알림 편지함 전체 조회",
            description = "읽음/안읽음을 함께 최신순으로 조회한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<List<NotificationSummaryResDto>> getNotifications(@Parameter(hidden = true) Long accountId);
}