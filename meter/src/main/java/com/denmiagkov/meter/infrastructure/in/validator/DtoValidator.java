package com.denmiagkov.meter.infrastructure.in.validator;

public interface DtoValidator<T>{
    boolean isValid(T object);
}
