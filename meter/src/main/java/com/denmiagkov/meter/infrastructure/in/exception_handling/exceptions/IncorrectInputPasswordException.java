package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

public class IncorrectInputPasswordException extends ValidationException{
    public IncorrectInputPasswordException() {
        super("Пароль пользователя должен содержать не менее 8 символов!");
    }
}
