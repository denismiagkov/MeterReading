package com.denmiagkov.meter.application.exception;
/**
 * Исключение, выбрасываемое при ошибке авторизации пользователя в системе
 * */
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
