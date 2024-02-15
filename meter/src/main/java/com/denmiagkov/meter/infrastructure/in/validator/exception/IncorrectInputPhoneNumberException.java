package com.denmiagkov.meter.infrastructure.in.validator.exception;

public class IncorrectInputPhoneNumberException extends RuntimeException {
    public IncorrectInputPhoneNumberException() {
        super("Ошибка ввода! Номер телефона может содержать только цифры и символ '+'!");
    }
}
