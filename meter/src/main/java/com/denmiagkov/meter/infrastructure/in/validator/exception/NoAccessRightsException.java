package com.denmiagkov.meter.infrastructure.in.validator.exception;

/**
 * Исключение, выбрасываемое при попытке доступа неуполномоченного пользователя к данным,
 * доступным только для администратора
 */
public class NoAccessRightsException extends RuntimeException {
    public NoAccessRightsException() {
        super("Only admin has access to this data!");
    }
}
