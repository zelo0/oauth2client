package com.fourpeople.runninghi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 자바스크립트 라이브러리(fetch, axios 등)으로 요청했을 때 응답을 받을 수 있게 할 지
        // 내 서버가 응답할 때 json을 자바스크립트에서 처리 가능하게 할 지 설정
        config.addAllowedHeader("*"); // 모든 헤더에 응답 허용
        config.addAllowedMethod("*"); // 모든 메소드(GET, POST 등)에 응답 허용
        config.addAllowedOrigin("*"); // 모든 ip에 응답 허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
