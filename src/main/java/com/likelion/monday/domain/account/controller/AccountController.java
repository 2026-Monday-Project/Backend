package com.likelion.monday.domain.account.controller;

import com.likelion.monday.domain.account.dto.AccountAvailabilityResDto;
import com.likelion.monday.domain.account.dto.AccountLoginReqDto;
import com.likelion.monday.domain.account.dto.AccountLoginResDto;
import com.likelion.monday.domain.account.dto.EmailCheckReqDto;
import com.likelion.monday.domain.account.dto.NicknameCheckReqDto;
import com.likelion.monday.domain.account.service.AccountService;
import com.likelion.monday.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountControllerDocs {

    private final AccountService accountService;

    @Override
    @GetMapping("/nickname-check")
    public ApiResponse<AccountAvailabilityResDto> checkNickname(@Valid @ModelAttribute NicknameCheckReqDto request) {
        return ApiResponse.success(new AccountAvailabilityResDto(accountService.isNicknameAvailable(request.nickname())));
    }

    @Override
    @GetMapping("/email-check")
    public ApiResponse<AccountAvailabilityResDto> checkEmail(@Valid @ModelAttribute EmailCheckReqDto request) {
        return ApiResponse.success(new AccountAvailabilityResDto(accountService.isEmailAvailable(request.email())));
    }

    @Override
    @PostMapping("/login")
    public ApiResponse<AccountLoginResDto> login(@Valid @RequestBody AccountLoginReqDto request) {
        return ApiResponse.success(accountService.login(request));
    }

    @Override
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        accountService.logout();
        return ApiResponse.success();
    }
}
