package com.libraryManagementSystem.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * com.libraryManagementSystem.service.impl.*.*(..))")
    private void publicMethodsFromServiceImplPackage() {
    }


    @Around("publicMethodsFromServiceImplPackage()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();

        if (methodName.startsWith("getAll")) {
            log.info(">> {}().start", methodName);
            Object result = joinPoint.proceed();
            log.info("<< {}().end", methodName);
            return result;
        }

        if (methodName.startsWith("delete") || methodName.equals("changePassword")
                || methodName.equals("updateAverageRating")) {
            log.info(">> {}().start - {}", methodName, Arrays.toString(args));
            Object result = joinPoint.proceed();
            log.info("<< {}().end", methodName);
            return result;
        }

        log.info(">> {}().start - {}", methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        log.info("<< {}().end - {}", methodName, result);
        return result;
    }
}
