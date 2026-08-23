package com.likelion.monday.domain.admin.controller;

import com.likelion.monday.domain.admin.dto.AdminLoginReqDto;
import com.likelion.monday.domain.admin.dto.AdminLoginResDto;
import com.likelion.monday.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin Auth", description = "관리자 로그인 API")
@RequestMapping("/admin")
public interface AdminAuthControllerDocs {

    @Operation(
            summary = "관리자 로그인",
            description = """
                    아이디와 비밀번호로 로그인하고 액세스 토큰을 발급받는다.
                    이후 관리자 API를 호출할 때 Authorization 헤더에 `Bearer {accessToken}` 형태로 담아 보낸다.
                    관리자 계정은 DB가 아닌 서버 설정에 보관한다.
                    """
    )
    ApiResponse<AdminLoginResDto> login(AdminLoginReqDto request);
}
