package com.likelion.monday.domain.admin.auth;

import com.likelion.monday.domain.admin.constant.AdminRole;
import com.likelion.monday.domain.admin.exception.AdminErrorCode;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 관리자 전용 엔드포인트 접근 제어.
 * Spring Security 필터 체인은 사용자 로그인 담당자와 함께 쓰는 설정이라 건드리지 않고,
 * /admin 경로에만 걸리는 인터셉터로 분리한다. 공통 인증 필터가 생기면 그쪽으로 옮긴다.
 */
@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 브라우저가 본 요청 전에 보내는 CORS 사전 요청에는 토큰이 실리지 않는다.
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String token = extractToken(request);
        if (!AdminRole.ADMIN.name().equals(jwtTokenProvider.getRole(token))) {
            throw new CustomException(AdminErrorCode.ADMIN_UNAUTHORIZED);
        }

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw new CustomException(AdminErrorCode.ADMIN_UNAUTHORIZED);
        }

        return header.substring(BEARER_PREFIX.length());
    }
}
