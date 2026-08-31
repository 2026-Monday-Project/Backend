package com.likelion.monday.domain.mygarden.controller;

import com.likelion.monday.domain.account.auth.LoginAccountId;
import com.likelion.monday.domain.mygarden.dto.*;
import com.likelion.monday.domain.mygarden.service.MyGardenService;
import com.likelion.monday.domain.story.entity.StoryStatus;
import com.likelion.monday.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MyGardenController implements MyGardenControllerDocs {

    private final MyGardenService myGardenService;

    @Override
    @GetMapping("/stories/preview")
    public ApiResponse<java.util.List<MyStorySummaryResDto>> getMyStoriesPreview(@LoginAccountId Long accountId) {
        return ApiResponse.success(myGardenService.getMyStoriesPreview(accountId));
    }

    @Override
    @GetMapping("/stories")
    public ApiResponse<PageResDto<MyStorySummaryResDto>> getMyStories(
            @LoginAccountId Long accountId,
            @RequestParam(required = false) StoryStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(myGardenService.getMyStories(accountId, status, page, size));
    }

    @Override
    @GetMapping("/summary")
    public ApiResponse<MyActivitySummaryResDto> getActivitySummary(@LoginAccountId Long accountId) {
        return ApiResponse.success(myGardenService.getActivitySummary(accountId));
    }

    @Override
    @GetMapping("/received-likes")
    public ApiResponse<PageResDto<ReceivedLikeResDto>> getReceivedLikes(
            @LoginAccountId Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(myGardenService.getReceivedLikes(accountId, page, size));
    }

    @Override
    @GetMapping("/liked-stories")
    public ApiResponse<PageResDto<LikedStoryResDto>> getLikedStories(
            @LoginAccountId Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(myGardenService.getLikedStories(accountId, page, size));
    }

    @Override
    @GetMapping("/stories/{storyId}")
    public ApiResponse<MyStoryDetailResDto> getMyStoryDetail(
            @LoginAccountId Long accountId,
            @PathVariable("storyId") Long storyId) {
        return ApiResponse.success(myGardenService.getMyStoryDetail(accountId, storyId));
    }

    @Override
    @DeleteMapping("/stories/{storyId}")
    public ApiResponse<Void> deleteMyStory(
            @LoginAccountId Long accountId,
            @PathVariable("storyId") Long storyId) {
        myGardenService.deleteMyStory(accountId, storyId);
        return ApiResponse.success();
    }

    @Override
    @GetMapping("/notifications/preview")
    public ApiResponse<java.util.List<NotificationSummaryResDto>> getNotificationsPreview(@LoginAccountId Long accountId) {
        return ApiResponse.success(myGardenService.getNotificationsPreview(accountId));
    }

    @Override
    @GetMapping("/notifications")
    public ApiResponse<PageResDto<NotificationSummaryResDto>> getNotifications(
            @LoginAccountId Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(myGardenService.getNotifications(accountId, page, size));
    }

    @Override
    @GetMapping("/notifications/{notificationId}")
    public ApiResponse<NotificationDetailResDto> getNotificationDetail(
            @LoginAccountId Long accountId,
            @PathVariable("notificationId") Long notificationId) {
        return ApiResponse.success(myGardenService.getNotificationDetail(accountId, notificationId));
    }
}