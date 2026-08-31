package com.likelion.monday.domain.account.auth;

import com.likelion.monday.domain.account.constant.AccountRole;
import com.likelion.monday.global.exception.CommonErrorCode;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@link LoginAccountId}가 붙은 Long 파라미터에 로그인한 계정 id를 채움.
 * Authorization 헤더의 Bearer 토큰을 검증하고, 사용자 토큰이 아니면 인증 실패로 처리.
 * 공통 인증 필터가 생길시 옮길 예정.
 */
@Component
@RequiredArgsConstructor
public class LoginAccountArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginAccountId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = extractToken(webRequest);

        if (!AccountRole.USER.name().equals(jwtTokenProvider.getRole(token))) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }

        return parseAccountId(token);
    }

    private String extractToken(NativeWebRequest webRequest) {
        String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }

        return header.substring(BEARER_PREFIX.length());
    }

    private Long parseAccountId(String token) {
        try {
            return Long.valueOf(jwtTokenProvider.getSubject(token));
        } catch (NumberFormatException e) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }
    }
}
