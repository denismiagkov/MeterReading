package com.denmiagkov.meter.utils.exception;

/**
 * Исключение, выбрасываемое в случае ошибки при попытке подключения к базе данных
 */
public class DatabaseConnectionNotEstablishedException extends RuntimeException {
    public DatabaseConnectionNotEstablishedException(String message) {
        super(message);
    }
}
