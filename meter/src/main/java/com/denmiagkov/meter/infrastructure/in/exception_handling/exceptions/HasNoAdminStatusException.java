package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

/**
 * Исключение, выбрасываемое при попытке доступа неуполномоченного пользователя к данным,
 * доступным только для администратора
 */
public class HasNoAdminStatusException extends RuntimeException {
    public HasNoAdminStatusException() {
        super("Only admin has access to this data!");
    }
}
