package com.denmiagkov.starter.logging.aspect;

import com.denmiagkov.starter.logging.aspect.annotations.EnableLogging;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class EnableLoggingCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getBeanFactory().getBeansWithAnnotation(EnableLogging.class).size() > 0;
    }
}
