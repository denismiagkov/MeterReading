package com.denmiagkov.meter.infrastructure.in.validator;

/**
 * Интерфейс классов-валидаторов входящих ДТО
 */
public interface DtoValidator<T> {
    boolean isValid(T object);
}
