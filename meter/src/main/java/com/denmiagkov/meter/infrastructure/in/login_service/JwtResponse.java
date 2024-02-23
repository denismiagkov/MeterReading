package com.denmiagkov.meter.infrastructure.in.login_service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Объект, содержащий access и refresh токены, возвращаемый пользователю в случае успешного прохождения актентификации.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
}
