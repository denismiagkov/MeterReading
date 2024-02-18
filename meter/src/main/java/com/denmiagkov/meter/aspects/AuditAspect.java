package com.denmiagkov.meter.aspects;

import com.denmiagkov.meter.application.dto.incoming.IncomingDto;
import com.denmiagkov.meter.application.service.UserActivityService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class AuditAspect {
    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);
    private static final String EXCEPTION_MESSAGE = "EXCEPTION OCCURRED: ";
    public static final String INCOMING_DTO_PARENT_CLASS = "com.denmiagkov.meter.application.dto.incoming.IncomingDto";
    private UserActivityService activityService;

    void setActivityService(UserActivityService activityService) {
        this.activityService = activityService;
    }

    @Pointcut("within(@com.denmiagkov.meter.aspects.annotations.Audit *) && execution(* * (..))")
    private void annotatedByAudit() {
    }

    @Around("annotatedByAudit()")
    public Object recordAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        try {
            Object[] args = joinPoint.getArgs();
            Class<?> clazz = Class.forName(INCOMING_DTO_PARENT_CLASS);
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
