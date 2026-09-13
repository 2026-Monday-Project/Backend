package com.likelion.monday.domain.account.auth;

import com.likelion.monday.domain.account.constant.AccountRole;
import com.likelion.monday.global.exception.CommonErrorCode;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.jwt.JwtErrorCode;
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
 * {@link OptionalLoginAccountId}가 붙은 Long 파라미터에 로그인한 계정 id를 채운다.
 * Authorization 헤더가 없으면 null(비로그인)을 반환한다.
 * 헤더가 있어도 토큰이 단순 만료(EXPIRED_TOKEN)된 경우에는 비로그인 사용자와 동일하게 처리한다.
 * PUBLIC API를 대상으로 하므로, 로그인 여부와 무관하게 응답 가능해야 하기 때문이다.
 * 다만 위조/형식 오류 등 그 외의 경우는 기존 로그인 API와 동일하게 인증 실패로 처리한다.
 */
@Component
@RequiredArgsConstructor
public class OptionalLoginAccountArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalLoginAccountId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }

        String token = header.substring(BEARER_PREFIX.length());
        try {
            if (!AccountRole.USER.name().equals(jwtTokenProvider.getRole(token))) {
                throw new CustomException(CommonErrorCode.UNAUTHORIZED);
            }
            return parseAccountId(token);
        } catch (CustomException e) {
            if (e.getErrorCode() == JwtErrorCode.EXPIRED_TOKEN) {
                return null;
            }
            throw e;
        }
    }

    private Long parseAccountId(String token) {
        try {
            return Long.valueOf(jwtTokenProvider.getSubject(token));
        } catch (NumberFormatException e) {
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }
    }
}