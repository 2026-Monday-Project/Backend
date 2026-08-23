package com.likelion.monday.domain.admin.controller;

import com.likelion.monday.domain.admin.constant.StoryStatusFilter;
import com.likelion.monday.domain.admin.dto.AdminStoryCountResDto;
import com.likelion.monday.domain.admin.dto.AdminStoryDetailResDto;
import com.likelion.monday.domain.admin.dto.AdminStoryPageResDto;
import com.likelion.monday.domain.admin.dto.NotificationDraftResDto;
import com.likelion.monday.domain.admin.dto.NotificationSendReqDto;
import com.likelion.monday.domain.admin.dto.NotificationSendResDto;
import com.likelion.monday.domain.admin.dto.StoryReviewReqDto;
import com.likelion.monday.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin", description = "관리자 사연 검수 API")
@RequestMapping("/admin/stories")
public interface AdminStoryControllerDocs {

    @Operation(
            summary = "사연 현황 조회",
            description = "검토중, 공개, 비공개 사연 건수를 집계해 관리자 메인 화면에 표시한다."
    )
    ApiResponse<AdminStoryCountResDto> getStoryCount();

    @Operation(
            summary = "사연 목록 조회",
            description = """
                    상태로 필터링한 사연 목록을 오래 제출된 순으로 조회한다.
                    status를 생략하면 검토중(PENDING)인 사연만 조회되고, ALL을 넘기면 전체를 조회한다.
                    """
    )
    ApiResponse<AdminStoryPageResDto> getStories(StoryStatusFilter status, int page, int size);

    @Operation(
            summary = "사연 상세 조회",
            description = "검수에 필요한 사연 전체 정보를 조회한다. 작성자 이메일과 동의 현황을 포함한다."
    )
    ApiResponse<AdminStoryDetailResDto> getStoryDetail(Long storyId);

    @Operation(
            summary = "사연 검수 결과 반영",
            description = """
                    사연을 공개(PUBLIC) 또는 비공개(PRIVATE)로 변경한다.
                    공개된 뒤 부적절하다고 판단된 사연을 비공개로 되돌리는 것도 이 API로 처리한다.
                    검토중(PENDING)으로는 되돌릴 수 없다.
                    """
    )
    ApiResponse<AdminStoryDetailResDto> reviewStory(Long storyId, StoryReviewReqDto request);

    @Operation(
            summary = "알림 문구 자동완성",
            description = """
                    사연의 검수 결과에 맞는 알림 제목과 내용 초안을 반환한다.
                    이 시점에는 저장되지 않으며, 관리자가 내용을 수정한 뒤 발송 API를 호출한다.
                    """
    )
    ApiResponse<NotificationDraftResDto> getNotificationDraft(Long storyId);

    @Operation(
            summary = "알림 발송",
            description = "관리자가 확인한 제목과 내용으로 사연 작성자의 편지함에 알림을 생성한다."
    )
    ApiResponse<NotificationSendResDto> sendNotification(Long storyId, NotificationSendReqDto request);
}
