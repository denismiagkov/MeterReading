package com.denmiagkov.meter.application.exception;

import com.denmiagkov.meter.domain.User;

/**
 * Исключение, выбрасываемое при новой попытке регистрации уже зарегистрированного пользователя
 * */
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(User user){
        super(String.format("Error! User %s has been registered", user));
    }
}
