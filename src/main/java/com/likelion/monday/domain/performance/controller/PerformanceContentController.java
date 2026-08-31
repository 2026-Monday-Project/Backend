package com.likelion.monday.domain.performance.controller;

import com.likelion.monday.domain.performance.dto.PerformanceContentResDto;
import com.likelion.monday.domain.performance.service.PerformanceContentService;
import com.likelion.monday.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PerformanceContentController implements PerformanceContentControllerDocs {

    private final PerformanceContentService performanceContentService;

    @Override
    @GetMapping("/one-song")
    public ApiResponse<PerformanceContentResDto> getPerformanceContent() {
        return ApiResponse.success(performanceContentService.getPerformanceContent());
    }
}