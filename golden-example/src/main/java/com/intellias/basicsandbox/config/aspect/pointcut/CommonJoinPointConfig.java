package com.intellias.basicsandbox.config.aspect.pointcut;


import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class CommonJoinPointConfig {

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceExecution() {
    }
}
