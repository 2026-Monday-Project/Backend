package com.likelion.monday.global.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    static final String DEFAULT_LOCAL_ORIGIN = "http://localhost:5173";

    private final List<String> allowedOrigins;
    private final Environment environment;

    public CorsConfig(
            @Value("${app.cors.allowed-origins:" + DEFAULT_LOCAL_ORIGIN + "}") List<String> allowedOrigins,
            Environment environment) {
        this.allowedOrigins = allowedOrigins;
        this.environment = environment;
    }

    /**
     * 운영(prod) 프로파일인데도 CORS origin이 로컬 기본값 하나뿐이면, 배포 도메인 설정을
     * 깜빡한 것이므로 조용히 요청이 막히게 두지 않고 서버 기동 자체를 막아 바로 알아채게 한다.
     * 로컬 개발 서버 접근 편의를 위해 배포 도메인과 함께 로컬 origin을 추가로 허용하는 것은
     * 정상적인 구성이라 막지 않는다.
     */
    @PostConstruct
    void validateProdOrigin() {
        boolean isProd = environment.acceptsProfiles(Profiles.of("prod"));
        if (isProd && allowedOrigins.equals(List.of(DEFAULT_LOCAL_ORIGIN))) {
            throw new IllegalStateException(
                    "운영 환경(prod)에서 app.cors.allowed-origins가 설정되지 않았습니다. "
                            + "실제 배포 프론트엔드 도메인을 명시적으로 설정해주세요.");
        }
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}