package com.example.shoppingmall.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("custom.url")
    private String url;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS 허용
                .allowedOrigins("http://localhost:3000") // 허용할 출처
                .allowedOrigins(url)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("") // 모든 헤더 허용
                .exposedHeaders("Authorization")
                .allowCredentials(true) // 인증 정보 포함 허용
                .maxAge(3600); // preflight 요청 캐시 시간
    }
}