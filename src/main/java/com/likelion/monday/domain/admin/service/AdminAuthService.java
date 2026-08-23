package com.likelion.monday.domain.admin.service;

import com.likelion.monday.domain.admin.constant.AdminRole;
import com.likelion.monday.domain.admin.dto.AdminLoginReqDto;
import com.likelion.monday.domain.admin.dto.AdminLoginResDto;
import com.likelion.monday.domain.admin.exception.AdminErrorCode;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 관리자 로그인.
 * 운영진 소수만 사용하는 페이지라 계정을 DB에 두지 않고 설정 파일에서 읽어 대조한다.
 * 비밀번호는 평문이 아닌 BCrypt 해시로 설정하며, 설정 값이 그대로 노출되어도 원문을 알 수 없게 한다.
 */
@Service
public class AdminAuthService {

    private static final String TOKEN_TYPE = "Bearer";

    /*
     * PasswordEncoder를 빈으로 등록하면 사용자 로그인 담당자가 같은 빈을 추가할 때 충돌할 수 있어
     * 관리자 인증에서만 쓰도록 직접 생성한다. 공통 빈이 생기면 주입받도록 바꾼다.
     */
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final JwtTokenProvider jwtTokenProvider;
    private final String adminUsername;
    private final String adminPassword;

    public AdminAuthService(JwtTokenProvider jwtTokenProvider,
                            @Value("${app.admin.username}") String adminUsername,
                            @Value("${app.admin.password}") String adminPassword) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public AdminLoginResDto login(AdminLoginReqDto request) {
        if (!isValidCredential(request)) {
            // 아이디가 틀렸는지 비밀번호가 틀렸는지 구분해서 알려주지 않는다.
            throw new CustomException(AdminErrorCode.ADMIN_LOGIN_FAILED);
        }

        String accessToken = jwtTokenProvider.createToken(adminUsername, AdminRole.ADMIN.name());

        return new AdminLoginResDto(accessToken, TOKEN_TYPE, jwtTokenProvider.getExpirationSeconds());
    }

    private boolean isValidCredential(AdminLoginReqDto request) {
        return adminUsername.equals(request.username())
                && passwordEncoder.matches(request.password(), adminPassword);
    }
}
