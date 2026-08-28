package com.likelion.monday.domain.mygarden.controller;

import com.likelion.monday.domain.account.auth.LoginAccountId;
import com.likelion.monday.domain.mygarden.dto.LikedStoryResDto;
import com.likelion.monday.domain.mygarden.dto.MyActivitySummaryResDto;
import com.likelion.monday.domain.mygarden.dto.MyStorySummaryResDto;
import com.likelion.monday.domain.mygarden.dto.ReceivedLikeResDto;
import com.likelion.monday.domain.mygarden.dto.SentStoryResDto;
import com.likelion.monday.domain.mygarden.service.MyGardenService;
import com.likelion.monday.domain.story.entity.StoryStatus;
import com.likelion.monday.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyGardenController implements MyGardenControllerDocs {

    private final MyGardenService myGardenService;

    @Override
    @GetMapping("/stories/preview")
    public ApiResponse<List<MyStorySummaryResDto>> getMyStoriesPreview(@LoginAccountId Long accountId) {
        return ApiResponse.success(myGardenService.getMyStoriesPreview(accountId));
    }

    @Override
    @GetMapping("/stories")
    public ApiResponse<List<MyStorySummaryResDto>> getMyStories(
            @LoginAccountId Long accountId,
            @RequestParam(required = false) StoryStatus status) {
        return ApiResponse.success(myGardenService.getMyStories(accountId, status));
    }

    @Override
    @GetMapping("/summary")
    public ApiResponse<MyActivitySummaryResDto> getActivitySummary(@LoginAccountId Long accountId) {
        return ApiResponse.success(myGardenService.getActivitySummary(accountId));
    }

    @Override
    @GetMapping("/sent-stories")
    public ApiResponse<List<SentStoryResDto>> getSentStories(@LoginAccountId Long accountId) {
        return ApiResponse.success(myGardenService.getSentStories(accountId));
    }

    @Override
    @GetMapping("/received-likes")
    public ApiResponse<List<ReceivedLikeResDto>> getReceivedLikes(@LoginAccountId Long accountId) {
        return ApiResponse.success(myGardenService.getReceivedLikes(accountId));
    }

    @Override
    @GetMapping("/liked-stories")
    public ApiResponse<List<LikedStoryResDto>> getLikedStories(@LoginAccountId Long accountId) {
        return ApiResponse.success(myGardenService.getLikedStories(accountId));
    }
}