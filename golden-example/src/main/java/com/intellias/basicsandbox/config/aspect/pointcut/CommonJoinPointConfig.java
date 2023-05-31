package com.intellias.basicsandbox.config.aspect.pointcut;


import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Config for enabling pointcut on @Service annotated classes
 */
@Component
public class CommonJoinPointConfig {

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceExecution() {
    }
}
