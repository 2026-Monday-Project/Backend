package com.likelion.monday.domain.account.controller;

import com.likelion.monday.domain.account.dto.AccountAvailabilityResDto;
import com.likelion.monday.domain.account.dto.AccountLoginReqDto;
import com.likelion.monday.domain.account.dto.AccountLoginResDto;
import com.likelion.monday.domain.account.dto.AccountProfileResDto;
import com.likelion.monday.domain.account.dto.EmailCheckReqDto;
import com.likelion.monday.domain.account.dto.MyNicknameCheckResDto;
import com.likelion.monday.domain.account.dto.NicknameCheckReqDto;
import com.likelion.monday.domain.account.dto.NicknameUpdateReqDto;
import com.likelion.monday.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Account", description = "사용자 계정/로그인 API")
@RequestMapping("/accounts")
public interface AccountControllerDocs {

    @Operation(
            summary = "닉네임 중복 확인",
            description = """
                    공개 닉네임으로 사용할 수 있는지 확인한다.
                    닉네임은 한글·영문·숫자 조합 10자 이내여야 하며, 이미 사용 중이면 available이 false로 내려온다.
                    """
    )
    ApiResponse<AccountAvailabilityResDto> checkNickname(NicknameCheckReqDto request);

    @Operation(
            summary = "이메일 중복 확인",
            description = """
                    로그인에 사용할 이메일을 다른 계정이 이미 쓰고 있는지 확인한다.
                    이미 사용 중이면 available이 false로 내려온다.
                    """
    )
    ApiResponse<AccountAvailabilityResDto> checkEmail(EmailCheckReqDto request);

    @Operation(
            summary = "내 프로필 조회",
            description = """
                    로그인한 사용자의 닉네임과 이메일을 한 번에 조회한다. 마이페이지 진입 시 사용한다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<AccountProfileResDto> getMyProfile(@Parameter(hidden = true) Long accountId);

    @Operation(
            summary = "닉네임 중복 확인 (로그인 사용자)",
            description = """
                    닉네임 변경 화면의 중복 확인 버튼에서 사용한다.
                    지금 본인이 쓰는 닉네임이면 available=true, current=true로 내려온다.
                    다른 계정이 쓰고 있으면 available=false, 아무도 안 쓰면 available=true(current=false)다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<MyNicknameCheckResDto> checkMyNickname(@Parameter(hidden = true) Long accountId, NicknameCheckReqDto request);

    @Operation(
            summary = "닉네임 변경",
            description = """
                    로그인한 사용자의 닉네임을 변경한다. 닉네임은 10자 이내여야 한다.
                    다른 계정이 이미 쓰는 닉네임이면 변경할 수 없다. 지금 쓰는 닉네임을 그대로 보내면 변경 없이 성공한다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<AccountProfileResDto> updateNickname(@Parameter(hidden = true) Long accountId, NicknameUpdateReqDto request);

    @Operation(
            summary = "이메일 로그인",
            description = """
                    사연을 제출할 때 사용한 이메일로 로그인하고 액세스 토큰을 발급받는다. 비밀번호는 없다.
                    사연을 한 번도 제출한 적 없는 이메일이면 로그인할 수 없다.
                    이후 요청의 Authorization 헤더에 `Bearer {accessToken}` 형태로 담아 보낸다.
                    """
    )
    ApiResponse<AccountLoginResDto> login(AccountLoginReqDto request);

    @Operation(
            summary = "로그아웃",
            description = """
                    서버에 토큰 상태를 두지 않는 stateless 로그인이라 별도 처리 없이 성공만 응답한다.
                    실제 로그아웃은 클라이언트가 보관 중인 토큰을 폐기하는 것으로 완료된다.
                    """
    )
    ApiResponse<Void> logout();
}
