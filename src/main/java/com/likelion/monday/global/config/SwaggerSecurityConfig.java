package com.likelion.monday.global.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger UI에 Authorize 버튼을 띄우기 위한 인증 방식 등록.
 * 토큰이 필요한 API에는 @SecurityRequirement(name = "bearerAuth")를 붙인다.
 */
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "관리자 로그인으로 받은 accessToken을 입력한다. Bearer 접두어는 자동으로 붙는다."
)
public class SwaggerSecurityConfig {
}
