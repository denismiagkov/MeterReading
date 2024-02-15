package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

public class IncorrectInputPhoneNumberException extends ValidationException {
    public IncorrectInputPhoneNumberException() {
        super("Номер телефона может содержать только цифры и символ '+'!");
    }
}
