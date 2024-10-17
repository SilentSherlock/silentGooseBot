package com.silent.silentgoosebot.others.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 */
@Configuration
@Slf4j
public class WebCorsConfig implements WebMvcConfigurer {

    @Value("${frontend_location}")
    private String origin;

    public void addCorsMappings(CorsRegistry registry) {
        log.info("origin: {}", origin);
        registry.addMapping("/**")
                .allowedOrigins(origin)
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
