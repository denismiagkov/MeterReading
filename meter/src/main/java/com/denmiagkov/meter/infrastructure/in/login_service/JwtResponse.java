package com.denmiagkov.meter.infrastructure.in.login_service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtResponse {
    public String accessToken;
    public String refreshToken;
}
