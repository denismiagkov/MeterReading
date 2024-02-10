package com.denmiagkov.meter.aspect;

import com.denmiagkov.meter.application.dto.*;
import com.denmiagkov.meter.application.repository.ActivityRepositoryImpl;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.UserActivityServiceImpl;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserAction;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Set;

@Aspect
public class AuditAspect {
    private final UserActivityService activityService;
//    private static final Class<?>[] INCOMING_DTO_SET = {
//            MeterReadingReviewActualDto.class,
//            MeterReadingReviewForMonthDto.class,
//            MeterReadingReviewHistoryDto.class,
//            MeterReadingSubmitDto.class,
//            UserIncomingDto.class,
//            UserLoginDto.class
//    };

    public AuditAspect() {
        this.activityService = new UserActivityServiceImpl(new ActivityRepositoryImpl());
    }

    @Pointcut("within(@com.denmiagkov.meter.aspect.annotations.Audit *) && execution(* * (..))")
    public void annotatedByAudit() {
    }

    public void recordAction(JoinPoint joinPoint) throws Throwable {
        Class<?> clazz = Class.forName("com.denmiagkov.meter.application.dto.IncomingDtoParent");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (clazz.isInstance(arg)) {
                IncomingDtoParent dto = (IncomingDtoParent) arg;
                int userId = dto.getUserId();
                ActionType action = dto.getAction();
                activityService.registerUserAction(userId, action);
            }
        }
    }
}
