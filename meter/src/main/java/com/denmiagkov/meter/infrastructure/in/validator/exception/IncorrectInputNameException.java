package com.denmiagkov.meter.infrastructure.in.validator.exception;

public class IncorrectInputNameException extends RuntimeException{
    public IncorrectInputNameException() {
        super("Ошибка ввода! Имя пользователя может содержать только буквы!");
    }
}
