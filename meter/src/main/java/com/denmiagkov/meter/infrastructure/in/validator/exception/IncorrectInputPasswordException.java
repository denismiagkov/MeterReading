package com.denmiagkov.meter.infrastructure.in.validator.exception;

public class IncorrectInputPasswordException extends RuntimeException{
    public IncorrectInputPasswordException() {
        super("Ошибка ввода! Пароль пользователя должен содержать не менее 8 символов!");
    }
}
