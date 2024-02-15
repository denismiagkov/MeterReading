package com.denmiagkov.meter.application.service.exception;

/**
 * Исключение, выбрасываемое при ошибке авторизации пользователя в системе
 */
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
        super("Ошибка авторизации: пользователя с указанными логином и паролем не существует!");
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
