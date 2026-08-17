package com.likelion.monday.global.storage;

import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * LocalImageStorage가 저장한 이미지를 base-url 경로로 내려주기 위한 정적 리소스 매핑.
 * S3 등 외부 저장소로 교체하면 이 설정은 필요 없어진다.
 */
@Configuration
public class LocalImageResourceConfig implements WebMvcConfigurer {

    private final String rootDir;
    private final String baseUrl;

    public LocalImageResourceConfig(@Value("${app.storage.local.root-dir:./uploads}") String rootDir,
                                    @Value("${app.storage.local.base-url:/images}") String baseUrl) {
        this.rootDir = rootDir;
        this.baseUrl = baseUrl;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = Paths.get(rootDir).toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler(baseUrl + "/**").addResourceLocations(location);
    }
}
