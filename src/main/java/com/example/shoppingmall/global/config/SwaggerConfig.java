package com.example.shoppingmall.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());

    }
    private Info apiInfo() {
        return new Info()
                .title("쇼핑몰 스웨거 페이지")
                .description("API 명세서")
                .version("1.0.0");
    }
}
