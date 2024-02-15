package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

public class IncorrectInputNameException extends ValidationException{
    public IncorrectInputNameException() {
        super("Имя пользователя может содержать только буквы!");
    }
}
