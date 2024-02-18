package com.denmiagkov.meter.utils.yaml_config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Authentication {
    private String valueOfJwtAccessSecretKey;
    private String valueOfJwtRefreshSecretKey;
}