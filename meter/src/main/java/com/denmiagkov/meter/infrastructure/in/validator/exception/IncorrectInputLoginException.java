package com.denmiagkov.meter.infrastructure.in.validator.exception;

public class IncorrectInputLoginException extends RuntimeException {
    public IncorrectInputLoginException() {
        super("Ошибка ввода! Логин пользователя не может быть пустым или состоять только из пробелов!");
    }
}
