package com.denmiagkov.starter.audit.config;

import com.denmiagkov.starter.audit.aspect.AuditAspect;
import com.denmiagkov.starter.audit.service.AuditService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditConfig<E extends Enum<E>> {

    @Bean
    @ConditionalOnBean(AuditService.class)
    AuditAspect<E> auditAspect(AuditService<E> service) {
        return new AuditAspect<>(service);
    }
}
