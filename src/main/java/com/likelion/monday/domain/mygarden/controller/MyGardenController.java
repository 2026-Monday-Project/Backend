package com.likelion.monday.domain.mygarden.controller;

import com.likelion.monday.domain.mygarden.dto.MyStorySummaryResDto;
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
    @GetMapping("/my-garden/stories/preview")
    public ApiResponse<List<MyStorySummaryResDto>> getMyStoriesPreview(@RequestParam Long accountId) {
        return ApiResponse.success(myGardenService.getMyStoriesPreview(accountId));
    }

    @Override
    @GetMapping("/my-garden/stories")
    public ApiResponse<List<MyStorySummaryResDto>> getMyStories(
            @RequestParam Long accountId,
            @RequestParam(required = false) StoryStatus status) {
        return ApiResponse.success(myGardenService.getMyStories(accountId, status));
    }
}