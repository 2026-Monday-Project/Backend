package com.likelion.monday.global.config;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@ExtendWith(MockitoExtension.class)
class CorsConfigTest {

    @Mock
    private Environment environment;

    @Test
    @DisplayName("prod 프로파일인데 origin이 로컬 기본값 그대로면 예외가 발생한다")
    void prod에서_기본_origin이면_예외() {
        given(environment.acceptsProfiles(any(Profiles.class))).willReturn(true);
        CorsConfig corsConfig = new CorsConfig(List.of(CorsConfig.DEFAULT_LOCAL_ORIGIN), environment);

        assertThatThrownBy(corsConfig::validateProdOrigin)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("prod 프로파일이어도 실제 배포 origin이 설정되어 있으면 정상 기동한다")
    void prod에서_실제_origin이면_정상() {
        given(environment.acceptsProfiles(any(Profiles.class))).willReturn(true);
        CorsConfig corsConfig = new CorsConfig(List.of("https://monday.example.com"), environment);

        assertThatCode(corsConfig::validateProdOrigin).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("prod 프로파일이 아니면 기본 origin이어도 정상 기동한다")
    void local에서는_기본_origin이어도_정상() {
        given(environment.acceptsProfiles(any(Profiles.class))).willReturn(false);
        CorsConfig corsConfig = new CorsConfig(List.of(CorsConfig.DEFAULT_LOCAL_ORIGIN), environment);

        assertThatCode(corsConfig::validateProdOrigin).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("prod 프로파일이어도 실제 배포 origin과 로컬 origin이 함께 있으면 정상 기동한다")
    void prod에서_실제_origin과_로컬_origin이_함께면_정상() {
        given(environment.acceptsProfiles(any(Profiles.class))).willReturn(true);
        CorsConfig corsConfig = new CorsConfig(
                List.of("https://monday.example.com", CorsConfig.DEFAULT_LOCAL_ORIGIN), environment);

        assertThatCode(corsConfig::validateProdOrigin).doesNotThrowAnyException();
    }
}