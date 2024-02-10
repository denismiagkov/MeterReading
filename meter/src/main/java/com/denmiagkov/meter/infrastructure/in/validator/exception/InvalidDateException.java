package com.denmiagkov.meter.infrastructure.in.validator.exception;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException() {
        super("Введен некорректный месяц для предоставления данных!");
    }
}
