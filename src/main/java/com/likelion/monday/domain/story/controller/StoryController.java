package com.likelion.monday.domain.story.controller;

import com.likelion.monday.domain.account.auth.LoginAccountId;
import com.likelion.monday.domain.account.auth.OptionalLoginAccountId;
import com.likelion.monday.domain.story.constant.StorySort;
import com.likelion.monday.domain.story.dto.StoryCreateReqDto;
import com.likelion.monday.domain.story.dto.StoryDetailResDto;
import com.likelion.monday.domain.story.dto.StoryPageResDto;
import com.likelion.monday.domain.story.dto.StoryUpdateReqDto;
import com.likelion.monday.domain.story.dto.StoryWriteResDto;
import com.likelion.monday.domain.story.service.StoryService;
import com.likelion.monday.global.cookie.GuestKeyProvider;
import com.likelion.monday.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class StoryController implements StoryControllerDocs {

    private final StoryService storyService;
    private final GuestKeyProvider guestKeyProvider;

    @Override
    @GetMapping
    public ApiResponse<StoryPageResDto> getStories(
            @RequestParam(defaultValue = "LATEST") StorySort sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(storyService.getStories(sort, page, size));
    }

    @Override
    @GetMapping("/{storyId}")
    public ApiResponse<StoryDetailResDto> getStory(
            @PathVariable Long storyId,
            HttpServletRequest request,
            HttpServletResponse response) {
        String guestKey = guestKeyProvider.resolve(request, response);
        return ApiResponse.success(storyService.getStory(storyId, guestKey));
    }

    @Override
    @PostMapping("/{storyId}/likes")
    public ApiResponse<Void> likeStory(
            @OptionalLoginAccountId Long accountId,
            @PathVariable Long storyId,
            HttpServletRequest request,
            HttpServletResponse response) {
        String guestKey = guestKeyProvider.resolve(request, response);
        storyService.likeStory(storyId, accountId, guestKey);
        return ApiResponse.success();
    }

    @Override
    @DeleteMapping("/{storyId}/likes")
    public ApiResponse<Void> unlikeStory(
            @OptionalLoginAccountId Long accountId,
            @PathVariable Long storyId,
            HttpServletRequest request,
            HttpServletResponse response) {
        String guestKey = guestKeyProvider.resolve(request, response);
        storyService.unlikeStory(storyId, accountId, guestKey);
        return ApiResponse.success();
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StoryWriteResDto> createStory(
            @Valid @RequestPart("request") StoryCreateReqDto request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return ApiResponse.success(storyService.createStory(request, images));
    }

    @Override
    @PatchMapping(value = "/{storyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<StoryWriteResDto> updateStory(
            @LoginAccountId Long accountId,
            @PathVariable Long storyId,
            @Valid @RequestPart("request") StoryUpdateReqDto request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return ApiResponse.success(storyService.updateStory(accountId, storyId, request, images));
    }
}
