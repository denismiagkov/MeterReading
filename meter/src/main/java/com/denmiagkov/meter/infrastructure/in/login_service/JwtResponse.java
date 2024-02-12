package com.denmiagkov.meter.infrastructure.in.login_service;

/**
 * Объект, содержащий access и refresh токены, возвращаемый пользователю в случае успешного прохождения актентификации.
 * */
public class JwtResponse {
    public String accessToken;
    public String refreshToken;

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
