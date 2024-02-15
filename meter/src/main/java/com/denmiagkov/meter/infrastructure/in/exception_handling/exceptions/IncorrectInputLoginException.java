package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

public class IncorrectInputLoginException extends ValidationException {
    public IncorrectInputLoginException() {
        super("Логин пользователя не может быть пустым или состоять только из пробелов!");
    }
}
