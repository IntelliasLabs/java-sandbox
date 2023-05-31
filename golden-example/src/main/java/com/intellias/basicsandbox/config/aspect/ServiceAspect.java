package com.intellias.basicsandbox.config.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Class used for aspect wrapping logging. In this example used @Service annotated classes logging
 * based on 'isLogEnabled' config property. You can disable by switching this config to 'false'
 */
@Component
@Aspect
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@ConditionalOnProperty(name = "app.dev.service-logging-enable", havingValue = "true")
public class ServiceAspect {

    // path to pointcut used in this Aspect
    private final static String SERVICE_POINTCUT = "com.intellias.basicsandbox.config.aspect.pointcut.CommonJoinPointConfig.serviceExecution()";

    /**
     * Log passed parameters to method before actual execution
     *
     * @param joinPoint JointPoint
     */
    @Before(SERVICE_POINTCUT)
    public void logMethodBefore(JoinPoint joinPoint) {
        log.info("Starting: method {} execution with params: {}",
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * Log returning parameters from method after actual execution
     *
     * @param joinPoint JointPoint
     * @param result    result of the method execution to log
     */
    @AfterReturning(pointcut = SERVICE_POINTCUT, returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        log.info("Return after execution method {}. Returned value: {}", joinPoint.getSignature(), result);
    }

    /**
     * Log exception while method execution, if thrown in method
     *
     * @param joinPoint JointPoint
     * @param error     Exception thrown by the method
     */
    @AfterThrowing(pointcut = SERVICE_POINTCUT, throwing = "error")
    public void logServiceAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.warn("Exception in method: {}. Cause: {}", joinPoint.getSignature(), error.getMessage());
    }
}
