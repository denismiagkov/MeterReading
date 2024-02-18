package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

public class IncorrectInputNameException extends ValidationException{
    public IncorrectInputNameException() {
        super("User name may contain only letters!");
    }
}
