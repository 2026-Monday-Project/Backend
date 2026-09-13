package com.likelion.monday.domain.account.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;

import com.likelion.monday.global.exception.CommonErrorCode;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.jwt.JwtErrorCode;
import com.likelion.monday.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.NativeWebRequest;

@ExtendWith(MockitoExtension.class)
class OptionalLoginAccountArgumentResolverTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private NativeWebRequest webRequest;

    private OptionalLoginAccountArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new OptionalLoginAccountArgumentResolver(jwtTokenProvider);
    }

    @Test
    @DisplayName("Authorization 헤더가 없으면 null을 반환한다")
    void 헤더_없으면_null() {
        given(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn(null);

        Object result = resolver.resolveArgument(null, null, webRequest, null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("만료된 토큰이면 비로그인 사용자와 동일하게 null을 반환한다")
    void 만료된_토큰이면_null() {
        given(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer expired-token");
        given(jwtTokenProvider.getRole("expired-token"))
                .willThrow(new CustomException(JwtErrorCode.EXPIRED_TOKEN));

        Object result = resolver.resolveArgument(null, null, webRequest, null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Bearer로 시작하지 않는 헤더면 인증 실패로 처리한다")
    void bearer_아니면_인증실패() {
        given(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Basic abcdef");

        CustomException exception = catchThrowableOfType(
                CustomException.class, () -> resolver.resolveArgument(null, null, webRequest, null));

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("위조되거나 형식이 깨진 토큰이면 인증 실패로 처리한다")
    void 위조된_토큰이면_인증실패() {
        given(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer broken-token");
        given(jwtTokenProvider.getRole("broken-token"))
                .willThrow(new CustomException(JwtErrorCode.INVALID_TOKEN));

        CustomException exception = catchThrowableOfType(
                CustomException.class, () -> resolver.resolveArgument(null, null, webRequest, null));

        assertThat(exception.getErrorCode()).isEqualTo(JwtErrorCode.INVALID_TOKEN);
    }

    @Test
    @DisplayName("USER가 아닌 역할의 토큰이면 인증 실패로 처리한다")
    void role_불일치면_인증실패() {
        given(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer admin-token");
        given(jwtTokenProvider.getRole("admin-token")).willReturn("ADMIN");

        CustomException exception = catchThrowableOfType(
                CustomException.class, () -> resolver.resolveArgument(null, null, webRequest, null));

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("유효한 사용자 토큰이면 계정 id를 반환한다")
    void 유효한_토큰이면_계정id_반환() {
        given(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer valid-token");
        given(jwtTokenProvider.getRole("valid-token")).willReturn("USER");
        given(jwtTokenProvider.getSubject("valid-token")).willReturn("42");

        Object result = resolver.resolveArgument(null, null, webRequest, null);

        assertThat(result).isEqualTo(42L);
    }
}
