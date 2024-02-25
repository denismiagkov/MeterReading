package com.denmiagkov.starter.audit.aspect;

import com.denmiagkov.starter.audit.dto.IncomingDto;
import com.denmiagkov.starter.audit.service.AuditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс-аспект, реализующий аудит действий пользователей. Содержит параматризированный тип,
 * определяющий виды совершаемых пользователями действий
 */
@Aspect
@AllArgsConstructor
@Slf4j
public class AuditAspect<E extends Enum<E>> {
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
                    IncomingDto<E> dto = (IncomingDto) arg;
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
