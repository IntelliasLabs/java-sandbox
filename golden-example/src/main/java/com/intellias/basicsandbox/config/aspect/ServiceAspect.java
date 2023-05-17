package com.intellias.basicsandbox.config.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ServiceAspect {

    @Value("${app.dev.service-logging-enable}")
    private boolean isLogEnabled;
    private final static String SERVICE_POINTCUT = "com.intellias.basicsandbox.config.aspect.pointcut.CommonJoinPointConfig.serviceExecution()";

    @Before(SERVICE_POINTCUT)
    public void logMethodBefore(JoinPoint joinPoint) {
        if (isLogEnabled) {
            log.info("Starting: method {} execution with params: {}",
                    joinPoint.getSignature(),
                    Arrays.toString(joinPoint.getArgs()));
        }
    }

    @AfterReturning(pointcut = SERVICE_POINTCUT, returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        if (isLogEnabled) {
            log.info("Return after execution method {}. Returned value: {}", joinPoint.getSignature(), result);
        }
    }

    @AfterThrowing(pointcut = SERVICE_POINTCUT, throwing = "error")
    public void logServiceAfterThrowing(JoinPoint joinPoint, Throwable error) {
        if (isLogEnabled) {
            log.warn("Exception in method: {}. Cause: {}", joinPoint.getSignature(), error.getMessage());
        }
    }
}
