package org.example.expert.aop;

// Jackson 라이브러리: 자바 객체를 JSON 형태의 문자열로 바꿔주는 도구입니다.
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j // Logger 클래스 활용
@Aspect // 공통 로직 모음 역할
@Component // Bean으로 등록
@RequiredArgsConstructor // 강제성 DI
public class AdminApiLoggingAspect {

    private final ObjectMapper objectMapper;

    // API 메서드만 타겟 지정
    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")

    public void adminApis() {} // 타겟을 묶어서

    @Around("adminApis()") // 실행 전, 후 모두 로깅 수행함
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable { // ProceedingJoinPoint: 현재 실행되려는 타겟 메서드의 정보(파라미터 등)를 담고 있는 객체

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 필요한 데이터 수집
        Long userId = (Long) request.getAttribute("userId");
        LocalDateTime requestTime = LocalDateTime.now();
        String requestUrl = request.getRequestURI();

        // 컨트롤러가 받을 RequestBody를 가로챔
        Object[] args = joinPoint.getArgs();

        // 파라미터가 존재하면 JSON 형식으로 변환, 없으면 비어있는 JSON "{}"
        String requestBodyJson = args.length > 0 ? objectMapper.writeValueAsString(args[0]) : "{}";

        // 수집한 데이터 출력
        log.info("=== [AOP: 어드민 API 요청 정보] ===");
        log.info("사용자 ID   : {}", userId);
        log.info("API 요청 시각: {}", requestTime);
        log.info("API 요청 URL: {}", requestUrl);
        log.info("요청 본문    : {}", requestBodyJson); // RequestBody

        // proceed()를 호출하는 시점에 실제 deleteComment나 changeUserRole 메서드가 실행됩니다.
        // 그리고 그 결과값(응답)을 response 변수에 저장
        Object response = joinPoint.proceed();

        // 컨트롤러가 반환한 응답값(response)을 JSON 형식으로 변환합니다.
        String responseBodyJson = response != null ? objectMapper.writeValueAsString(response) : "{}";

        log.info("=== [AOP: 어드민 API 응답 정보] ===");
        log.info("응답 본문    : {}", responseBodyJson); // 5. 응답 본문(ResponseBody)

        // 가로챘던 응답값을 원래 목적지(클라이언트)로 정상적으로 돌려줍니다.
        return response;
    }
}