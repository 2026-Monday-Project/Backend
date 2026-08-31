package com.likelion.monday.global.config;

import com.likelion.monday.domain.account.auth.OptionalLoginAccountId;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import java.util.Arrays;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * @OptionalLoginAccountId를 쓰는 API는 인증이 필수가 아니므로,
 * OpenAPI 스펙의 security를 [{"bearerAuth":[]}, {}]로 만들어 "토큰이 있어도 되고 없어도 됨"을 정확히 표현한다.
 */
@Component
public class OptionalAuthOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        boolean hasOptionalLogin = Arrays.stream(handlerMethod.getMethodParameters())
                .anyMatch(parameter -> parameter.hasParameterAnnotation(OptionalLoginAccountId.class));

        if (hasOptionalLogin && operation.getSecurity() != null) {
            operation.getSecurity().add(new SecurityRequirement());
        }

        return operation;
    }
}