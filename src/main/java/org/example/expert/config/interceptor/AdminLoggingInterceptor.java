package org.example.expert.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.InvalidRequestException; // 프로젝트의 예외 클래스
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j // logger 클래스 활용
@Component // Bean으로 등록
public class AdminLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 이전 단계(필터 등)에서 request에 담아둔 사용자의 권한 정보를 꺼내옵니다.
        String userRole = (String) request.getAttribute("userRole");

        // ADMIN이 아니면
        if (!"ADMIN".equals(userRole)) {
            throw new InvalidRequestException("관리자 권한이 필요합니다.");
        }

        // ADMIN이면!
        log.info("=== [Interceptor: 어드민 인증 성공] ===");
        log.info("요청 시각: {}", LocalDateTime.now());
        log.info("요청 URL : {}", request.getRequestURI());

        return true;
    }
}