package com.likelion.monday.domain.admin.controller;

import com.likelion.monday.domain.admin.dto.AdminLoginReqDto;
import com.likelion.monday.domain.admin.dto.AdminLoginResDto;
import com.likelion.monday.domain.admin.service.AdminAuthService;
import com.likelion.monday.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminAuthController implements AdminAuthControllerDocs {

    private final AdminAuthService adminAuthService;

    @Override
    @PostMapping("/login")
    public ApiResponse<AdminLoginResDto> login(@Valid @RequestBody AdminLoginReqDto request) {
        return ApiResponse.success(adminAuthService.login(request));
    }
}
