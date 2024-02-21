package com.denmiagkov.meter.config.yaml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "authentication")
public class AuthConfig {
    private String valueOfJwtAccessSecretKey;
    private String valueOfJwtRefreshSecretKey;
}
