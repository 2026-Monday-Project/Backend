package com.likelion.monday.global.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * multipart 요청의 JSON 파트를 application/octet-stream으로도 읽을 수 있게 한다.
 * 브라우저와 Swagger UI는 파일이 아닌 파트에 Content-Type을 붙이지 않는 경우가 많은데,
 * 그때 JSON 컨버터가 파트를 처리하지 못해 415가 발생하기 때문이다.
 */
@Configuration
public class MultipartJsonConverterConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (!(converter instanceof AbstractHttpMessageConverter<?> jsonConverter)) {
                continue;
            }
            if (!jsonConverter.getSupportedMediaTypes().contains(MediaType.APPLICATION_JSON)) {
                continue;
            }

            // application/json을 앞에 둔 채 뒤에만 덧붙여, 응답 협상 결과가 바뀌지 않도록 한다.
            List<MediaType> mediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
            if (!mediaTypes.contains(MediaType.APPLICATION_OCTET_STREAM)) {
                mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
                jsonConverter.setSupportedMediaTypes(mediaTypes);
            }
        }
    }
}
