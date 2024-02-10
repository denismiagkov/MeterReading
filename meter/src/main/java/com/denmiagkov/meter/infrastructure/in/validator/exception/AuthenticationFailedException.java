package com.denmiagkov.meter.infrastructure.in.validator.exception;
/**
 * Исключение, выбрасываемое при ошибке авторизации пользователя в системе
 * */
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
        super("Ошибка авторизации: пользователя с указанными логином и паролем не существует!");
    }
}
