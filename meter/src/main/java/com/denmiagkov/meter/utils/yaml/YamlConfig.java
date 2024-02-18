package com.denmiagkov.meter.utils.yaml;

import lombok.*;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class YamlConfig {
    private Datasource datasource;
    private Liquibase liquibase;
    private Authentication authentication;
}
