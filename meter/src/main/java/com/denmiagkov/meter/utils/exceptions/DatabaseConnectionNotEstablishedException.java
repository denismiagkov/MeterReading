package com.denmiagkov.meter.utils.exceptions;

/**
 * Исключение, выбрасываемое в случае ошибки при попытке подключения к базе данных
 */
public class DatabaseConnectionNotEstablishedException extends RuntimeException {
    public DatabaseConnectionNotEstablishedException(String message) {
        super(message);
    }
}
