package com.denmiagkov.meter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggableAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggableAspect.class);

    @Pointcut("within(@com.denmiagkov.meter.aspect.annotations.Loggable *) && execution(* * (..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Calling method " + proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        Long end = System.currentTimeMillis() - start;
        log.info("Execution of method %s finished. Execution time is %d ms."
                .formatted(proceedingJoinPoint.getSignature(), end));
        return result;
    }

}
