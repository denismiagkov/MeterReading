package com.denmiagkov.meter.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Конфигурационные данные SpringDoc
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Meter Reading Service",
                description = "Meter Reading Monitoring Service", version = "1.0.0",
                contact = @Contact(name = "Denis Miagkov", email = "medny2012@yandex.ru")
        ))
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
public class SpringDocConfig {

}