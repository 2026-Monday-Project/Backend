package com.likelion.monday.global.jwt;

import com.likelion.monday.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 발급과 검증을 담당한다.
 * 관리자 로그인과 사용자 이메일 로그인이 함께 사용하도록 global에 둔다.
 * 토큰에는 식별자(subject)와 권한(role)만 담고, 개인정보는 넣지 않는다.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private static final String ROLE_CLAIM = "role";

    private final SecretKey secretKey;
    private final Duration expiration;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret,
                            @Value("${app.jwt.expiration-minutes:720}") long expirationMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = Duration.ofMinutes(expirationMinutes);
    }

    public String createToken(String subject, String role) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + expiration.toMillis());

        return Jwts.builder()
                .subject(subject)
                .claim(ROLE_CLAIM, role)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(secretKey)
                .compact();
    }

    public String getRole(String token) {
        return parseClaims(token).get(ROLE_CLAIM, String.class);
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public long getExpirationSeconds() {
        return expiration.toSeconds();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new CustomException(JwtErrorCode.EXPIRED_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            // 위조되었거나 형식이 깨진 토큰이다. 원인을 응답에 노출하지 않고 로그로만 남긴다.
            log.warn("유효하지 않은 토큰입니다.", e);
            throw new CustomException(JwtErrorCode.INVALID_TOKEN);
        }
    }
}
