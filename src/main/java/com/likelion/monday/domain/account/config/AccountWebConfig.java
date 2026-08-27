package com.likelion.monday.domain.account.config;

import com.likelion.monday.domain.account.auth.LoginAccountArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AccountWebConfig implements WebMvcConfigurer {

    private final LoginAccountArgumentResolver loginAccountArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginAccountArgumentResolver);
    }
}
