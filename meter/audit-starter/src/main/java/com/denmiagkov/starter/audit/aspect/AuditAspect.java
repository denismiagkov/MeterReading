package com.denmiagkov.starter.audit.aspect;

import com.denmiagkov.starter.audit.dto.IncomingDto;
import com.denmiagkov.starter.audit.service.AuditService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@AllArgsConstructor
public class AuditAspect<E extends Enum<E>> {
    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);
    private static final String EXCEPTION_MESSAGE = "EXCEPTION OCCURRED: ";
    public static final String INCOMING_DTO_PARENT_CLASS = "com.denmiagkov.starter.audit.dto.IncomingDto";
    private AuditService<E> activityService;

    @Pointcut("within(@com.denmiagkov.starter.audit.aspect.annotations.Audit *) && execution(* * (..))")
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
                    IncomingDto<E> dto = (IncomingDto<E>) arg;
                    activityService.registerUserAction(dto);
                    break;
                }
            }
        } catch (Exception e) {
            log.error(EXCEPTION_MESSAGE, e);
        }
        return result;
    }
}
