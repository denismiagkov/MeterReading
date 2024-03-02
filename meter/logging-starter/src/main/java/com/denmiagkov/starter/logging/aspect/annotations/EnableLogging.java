package com.denmiagkov.starter.logging.aspect.annotations;

import com.denmiagkov.starter.logging.config.LoggingConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Класс создает аннотацию, которая реализует импорт в проект бинов, сконфигурированных в стартере логирования
 * */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Import(LoggingConfig.class)
public @interface EnableLogging {
}
