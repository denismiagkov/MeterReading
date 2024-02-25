package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

/**
 * Исключение, выбрасываемое при попытке доступа неуполномоченного пользователя к данным,
 * доступным только для администратора
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super("Data input error: " + message);
    }
}
