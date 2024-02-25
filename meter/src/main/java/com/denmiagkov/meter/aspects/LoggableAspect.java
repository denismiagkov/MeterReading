package com.denmiagkov.meter.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Класс-аспект, реализующий сквозное логирование работы методов
 */
@Aspect
@Component
public class LoggableAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggableAspect.class);

    @Pointcut("within(@com.denmiagkov.meter.aspects.annotations.Loggable *) && execution(* * (..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Calling method " + proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        Long executionTime = System.currentTimeMillis() - start;
        log.info("Execution of method %s finished. Execution time is %d ms."
                .formatted(proceedingJoinPoint.getSignature(), executionTime));
        return result;
    }
}
