package com.abbtech.aspect;

import com.abbtech.logging.LogSanitizer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.*;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiLoggingAspect {

    @Around("execution(* com.abbtech.controller..*(..))")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        assert attrs != null;
        HttpServletRequest request = attrs.getRequest();

        // 🔹 Incoming request log
        log.info("➡️ [{}] {}", request.getMethod(), request.getRequestURI());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                log.info("Request Body: {}", LogSanitizer.toJson(arg));
            }
        }

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long time = System.currentTimeMillis() - start;

        // 🔹 Outgoing response log
        log.info("⬅️ Response ({} ms): {}", time, LogSanitizer.toJson(result));

        return result;
    }
}
