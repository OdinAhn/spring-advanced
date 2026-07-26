package org.example.expert.config;

import lombok.RequiredArgsConstructor;

import org.example.expert.config.interceptor.AdminLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 환경설정 공간 -> 서버가 켜질때
@RequiredArgsConstructor // final 변수 생성자 DI

public class WebConfig implements WebMvcConfigurer {

    // 로깅 받아오고
    private final AdminLoggingInterceptor adminLoggingInterceptor;

    @Override // 재정의
    public void addInterceptors(InterceptorRegistry registry) { // 스프링 메서드

        registry.addInterceptor(adminLoggingInterceptor) // 등록
                .addPathPatterns("/admin/**"); // admin만 등록

        // 만약 특정 주소는 인터셉터를 안 거치게 하고 싶다면
        // .excludePathPatterns("/admin/login") 처럼 뺄 수도 있음
    }
}