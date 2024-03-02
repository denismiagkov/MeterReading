package com.denmiagkov.meter.config.yaml;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационные данные Liquibase
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.liquibase")
public class LiquibaseConfig {
    private String changelog;
    private String defaultSchema;
    private String liquibaseSchema;
}
