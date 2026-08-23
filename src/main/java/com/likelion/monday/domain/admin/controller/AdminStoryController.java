package com.likelion.monday.domain.admin.controller;

import com.likelion.monday.domain.admin.constant.StoryStatusFilter;
import com.likelion.monday.domain.admin.dto.AdminStoryCountResDto;
import com.likelion.monday.domain.admin.dto.AdminStoryDetailResDto;
import com.likelion.monday.domain.admin.dto.AdminStoryPageResDto;
import com.likelion.monday.domain.admin.dto.NotificationDraftResDto;
import com.likelion.monday.domain.admin.dto.NotificationSendReqDto;
import com.likelion.monday.domain.admin.dto.NotificationSendResDto;
import com.likelion.monday.domain.admin.dto.StoryReviewReqDto;
import com.likelion.monday.domain.admin.service.AdminStoryService;
import com.likelion.monday.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminStoryController implements AdminStoryControllerDocs {

    private final AdminStoryService adminStoryService;

    @Override
    @GetMapping("/summary")
    public ApiResponse<AdminStoryCountResDto> getStoryCount() {
        return ApiResponse.success(adminStoryService.getStoryCount());
    }

    @Override
    @GetMapping
    public ApiResponse<AdminStoryPageResDto> getStories(
            @RequestParam(defaultValue = "PENDING") StoryStatusFilter status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(adminStoryService.getStories(status, page, size));
    }

    @Override
    @GetMapping("/{storyId}")
    public ApiResponse<AdminStoryDetailResDto> getStoryDetail(@PathVariable Long storyId) {
        return ApiResponse.success(adminStoryService.getStoryDetail(storyId));
    }

    @Override
    @PatchMapping("/{storyId}/review")
    public ApiResponse<AdminStoryDetailResDto> reviewStory(
            @PathVariable Long storyId,
            @Valid @RequestBody StoryReviewReqDto request) {
        return ApiResponse.success(adminStoryService.reviewStory(storyId, request));
    }

    @Override
    @GetMapping("/{storyId}/notification-draft")
    public ApiResponse<NotificationDraftResDto> getNotificationDraft(@PathVariable Long storyId) {
        return ApiResponse.success(adminStoryService.getNotificationDraft(storyId));
    }

    @Override
    @PostMapping("/{storyId}/notifications")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<NotificationSendResDto> sendNotification(
            @PathVariable Long storyId,
            @Valid @RequestBody NotificationSendReqDto request) {
        return ApiResponse.success(adminStoryService.sendNotification(storyId, request));
    }
}
