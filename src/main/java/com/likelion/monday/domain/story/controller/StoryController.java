package com.likelion.monday.domain.story.controller;

import com.likelion.monday.domain.story.dto.StoryCreateReqDto;
import com.likelion.monday.domain.story.dto.StoryUpdateReqDto;
import com.likelion.monday.domain.story.dto.StoryWriteResDto;
import com.likelion.monday.domain.story.service.StoryService;
import com.likelion.monday.global.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class StoryController implements StoryControllerDocs {

    private final StoryService storyService;

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
            @PathVariable Long storyId,
            @Valid @RequestPart("request") StoryUpdateReqDto request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return ApiResponse.success(storyService.updateStory(storyId, request, images));
    }
}
