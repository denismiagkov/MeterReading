package com.denmiagkov.meter.infrastructure.in.login_service;

import lombok.AllArgsConstructor;

/**
 * Объект, содержащий access и refresh токены, возвращаемый пользователю в случае успешного прохождения актентификации.
 */
@AllArgsConstructor
public class JwtResponse {
    public String accessToken;
    public String refreshToken;
}
