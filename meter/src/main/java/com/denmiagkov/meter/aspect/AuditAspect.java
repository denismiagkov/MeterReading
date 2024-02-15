package com.denmiagkov.meter.aspect;

import com.denmiagkov.meter.application.dto.incoming.IncomingDto;
import com.denmiagkov.meter.application.service.UserActivityService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {
    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);
    private static final String EXCEPTION_MESSAGE = "EXCEPTION OCCURRED: ";
    private final UserActivityService activityService;

    @Autowired
    private AuditAspect(UserActivityService activityService) {
        this.activityService = activityService;
    }

    @Pointcut("within(@com.denmiagkov.meter.aspect.annotations.Audit *) && execution(* * (..))")
    private void annotatedByAudit() {
    }

    @Around("annotatedByAudit()")
    public Object recordAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        try {
            Object[] args = joinPoint.getArgs();
            Class<?> clazz = Class.forName("com.denmiagkov.meter.application.dto.incoming.IncomingDto");
            for (Object arg : args) {
                if (clazz.isInstance(arg)) {
                    IncomingDto dto = (IncomingDto) arg;
                    activityService.registerUserAction(dto);
                }
            }
        } catch (Exception e) {
            log.error(EXCEPTION_MESSAGE, e);
        }
        return result;
    }
}
