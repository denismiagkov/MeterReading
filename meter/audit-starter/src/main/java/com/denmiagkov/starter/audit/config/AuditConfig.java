package com.denmiagkov.starter.audit.config;

import com.denmiagkov.starter.audit.aspect.AuditAspect;
import com.denmiagkov.starter.audit.service.AuditService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditConfig {

    @Bean
    @ConditionalOnBean(AuditService.class)
    AuditAspect auditAspect(AuditService service){
        return new AuditAspect(service);
    }
}
