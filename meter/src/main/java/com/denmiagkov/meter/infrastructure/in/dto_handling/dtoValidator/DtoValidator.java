package com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator;

/**
 * Интерфейс классов-валидаторов входящих ДТО
 */
public interface DtoValidator<T> {
    boolean isValid(T object);
}
