package com.likelion.monday.global.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * 비로그인 사용자를 식별하기 위한 guest_key를 쿠키로 관리한다.
 * 요청에 쿠키가 있으면 그 값을 그대로 쓰고, 없으면 새로 발급해서 응답에 심어준다.
 * 조회/공감 등 중복 방지가 필요한 곳에서 공통으로 사용한다.
 *
 * 운영(prod)에서는 FE와 BE가 서로 다른 도메인이라 cross-site 요청이 되므로
 * SameSite=None; Secure로 내려야 브라우저가 쿠키를 저장·전송한다.
 * 로컬(local)은 http라 Secure 쿠키가 저장되지 않으므로 SameSite=Lax; Secure=false를 쓴다.
 */
@Component
public class GuestKeyProvider {

    private static final String COOKIE_NAME = "guest_key";
    private static final int COOKIE_MAX_AGE_SECONDS = 60 * 60 * 24 * 365;

    private final Environment environment;

    public GuestKeyProvider(Environment environment) {
        this.environment = environment;
    }

    public String resolve(HttpServletRequest request, HttpServletResponse response) {
        String existing = findExisting(request);
        if (existing != null) {
            return existing;
        }

        String newGuestKey = UUID.randomUUID().toString();
        response.addHeader(HttpHeaders.SET_COOKIE, createCookie(newGuestKey).toString());
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

    private ResponseCookie createCookie(String guestKey) {
        boolean isProd = environment.acceptsProfiles(Profiles.of("prod"));

        return ResponseCookie.from(COOKIE_NAME, guestKey)
                .httpOnly(true)
                .path("/")
                .maxAge(COOKIE_MAX_AGE_SECONDS)
                .secure(isProd)
                .sameSite(isProd ? "None" : "Lax")
                .build();
    }
}