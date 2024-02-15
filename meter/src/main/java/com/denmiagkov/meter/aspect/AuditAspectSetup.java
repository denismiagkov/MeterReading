package com.denmiagkov.meter.aspect;

import com.denmiagkov.meter.application.service.UserActivityService;
import org.aspectj.lang.Aspects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AuditAspectSetup {
    private final UserActivityService activityService;

    @Autowired
    private AuditAspectSetup(UserActivityService activityService) {
        this.activityService = activityService;
    }

    @PostConstruct
    private void getAuditAspect() {
        AuditAspect auditAspect = Aspects.aspectOf(AuditAspect.class);
        auditAspect.setActivityService(this.activityService);
    }
}


