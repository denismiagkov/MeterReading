package com.denmiagkov.meter.application.exception;
/**
 * Исключение, выбрасываемое при новой попытке регистрации уже зарегистрированного пользователя
 * */
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
