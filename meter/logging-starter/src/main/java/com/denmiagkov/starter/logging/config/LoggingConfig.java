package com.denmiagkov.starter.logging.config;

import com.denmiagkov.starter.logging.aspect.EnableLoggingCondition;
import com.denmiagkov.starter.logging.aspect.LoggableAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    /**
     * Бин аспекта логирования, создаваемый при условии наличия в контексте приложения бина, помеченного аннотацией EnableLogging
     *
     * @see EnableLoggingCondition
     */
    @Bean
    @Conditional(EnableLoggingCondition.class)
    public LoggableAspect startLogging() {
        return new LoggableAspect();
    }
}
