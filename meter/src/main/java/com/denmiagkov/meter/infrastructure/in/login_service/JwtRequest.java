package com.denmiagkov.meter.infrastructure.in.login_service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class JwtRequest {
    private String login;
    private String password;
}
