package com.denmiagkov.meter.application.service.exceptions;

/**
 * Исключение, выбрасываемое при ошибке аутентификации пользователя в системе
 */
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
        super("Ошибка аутентификации: неверные логин и пароль!");
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
