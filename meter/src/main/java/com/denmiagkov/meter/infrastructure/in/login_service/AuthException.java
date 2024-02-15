package com.denmiagkov.meter.infrastructure.in.login_service;

/**
 * Исключение, выбрасываемое при вводе пользователем неверного пароля
 */
public class AuthException extends RuntimeException {
    public AuthException() {
        super("Аутентификация не пройдена: введены неверные логин и пароль пользователя!");
    }
}