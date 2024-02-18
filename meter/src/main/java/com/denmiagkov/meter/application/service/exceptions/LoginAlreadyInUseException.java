package com.denmiagkov.meter.application.service.exceptions;

/**
 * Исключение, выбрасываемое при регистрации новой учетной записи пользователя,
 * в случае если указанный логин уже зарегистрирован в системе
 */
public class LoginAlreadyInUseException extends RuntimeException {
    public LoginAlreadyInUseException(String login) {
        super(String.format("Error! User with login \"%s\" has been registered", login));
    }
}
