package com.joyfarm.farmstival.global.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig {

    @Value("${cors.allow.origins}")
    private String allowedOrigins;

    // Cors 관련 헤더 -> 응답 헤더에 추가한다. (서버가 자원을 줄지 말지를 알려주는 것이므로)
        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedMethod("*"); // 모든 요청 메서드를 허용하겠다.
            config.addAllowedHeader("*"); // 모든 요청 헤더를 허용하겠다.
            config.addAllowedOrigin(allowedOrigins); // 개발할 때는 전체를 다 허용

           return new CorsFilter(source);
        }
    }
