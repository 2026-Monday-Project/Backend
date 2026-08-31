package com.likelion.monday.global.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * 비로그인 사용자를 식별하기 위한 guest_key를 쿠키로 관리한다.
 * 요청에 쿠키가 있으면 그 값을 그대로 쓰고, 없으면 새로 발급해서 응답에 심어준다.
 * 조회/공감 등 중복 방지가 필요한 곳에서 공통으로 사용한다.
 *
 * TODO: 배포 도메인이 FE와 분리되는 경우 SameSite=None; Secure 설정이 필요할 수 있음 (CORS/withCredentials 논의와 함께 확인)
 */
@Component
public class GuestKeyProvider {

    private static final String COOKIE_NAME = "guest_key";
    private static final int COOKIE_MAX_AGE_SECONDS = 60 * 60 * 24 * 365;

    public String resolve(HttpServletRequest request, HttpServletResponse response) {
        String existing = findExisting(request);
        if (existing != null) {
            return existing;
        }

        String newGuestKey = UUID.randomUUID().toString();
        response.addCookie(createCookie(newGuestKey));
        return newGuestKey;
    }

    private String findExisting(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private Cookie createCookie(String guestKey) {
        Cookie cookie = new Cookie(COOKIE_NAME, guestKey);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
        return cookie;
    }
}