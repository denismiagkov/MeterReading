package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

public class InvalidDateException extends ValidationException {
    public InvalidDateException() {
        super("Введен некорректный месяц для предоставления данных!");
    }
}
