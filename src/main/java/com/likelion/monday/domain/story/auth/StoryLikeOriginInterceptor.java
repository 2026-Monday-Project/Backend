package com.likelion.monday.domain.story.auth;

import com.likelion.monday.global.config.CorsConfig;
import com.likelion.monday.global.exception.CommonErrorCode;
import com.likelion.monday.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 사연 공감 등록/취소는 guest_key 쿠키만으로 사용자를 식별하는데,
 * 운영에서는 이 쿠키가 SameSite=None으로 내려가 cross-site 요청에도 자동으로 실린다.
 * CSRF 보호가 꺼져 있는 상태라(SecurityConfig), 허용된 origin에서 온 요청인지 여기서 직접 검증한다.
 * Origin 헤더가 없는 요청(Postman, 서버 간 호출 등 브라우저가 아닌 클라이언트)은 그대로 통과시킨다 —
 * 이 취약점은 브라우저가 쿠키를 "자동으로" 실어 보내는 경우에만 성립하기 때문이다.
 */
@Component
public class StoryLikeOriginInterceptor implements HandlerInterceptor {

    private final List<String> allowedOrigins;

    public StoryLikeOriginInterceptor(
            @Value("${app.cors.allowed-origins:" + CorsConfig.DEFAULT_LOCAL_ORIGIN + "}") List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String origin = request.getHeader("Origin");
        if (origin != null && !isAllowed(origin, request)) {
            throw new CustomException(CommonErrorCode.FORBIDDEN);
        }

        return true;
    }

    private boolean isAllowed(String origin, HttpServletRequest request) {
        return allowedOrigins.contains(origin) || origin.equals(selfOrigin(request));
    }

    private String selfOrigin(HttpServletRequest request) {
        String scheme = request.getScheme();
        boolean isDefaultPort = ("http".equals(scheme) && request.getServerPort() == 80)
                || ("https".equals(scheme) && request.getServerPort() == 443);

        return scheme + "://" + request.getServerName() + (isDefaultPort ? "" : ":" + request.getServerPort());
    }
}