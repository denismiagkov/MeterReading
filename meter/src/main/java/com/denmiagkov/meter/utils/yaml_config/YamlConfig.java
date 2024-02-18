package com.denmiagkov.meter.utils.yaml_config;

import lombok.*;

/**
 * Конфигурация приложения, вынесенная во внешний файл
 */

@NoArgsConstructor
@Data
public class YamlConfig {
    private Datasource datasource;
    private Liquibase liquibase;
    private Authentication authentication;

    @NoArgsConstructor
    @Data
    public static class Datasource {
        private String url;
        private String username;
        private String password;
    }

    @NoArgsConstructor
    @Data
    public static class Liquibase {
        private String serviceSchemaName;
        private String defaultSchemaName;
        private String changelogFile;
    }

    @NoArgsConstructor
    @Data
    public static class Authentication {
        private String valueOfJwtAccessSecretKey;
        private String valueOfJwtRefreshSecretKey;
    }
}
