package com.denmiagkov.meter.application.exception;

/**
 * Исключение, выбрасываемое при регистрации новой учетной записи пользователя,
 * в случае если указанный логин уже зарегистрирован в системе
 */
public class LoginAlreadyInUseException extends RuntimeException {
    public LoginAlreadyInUseException(String message) {
        super(message);
    }
}
