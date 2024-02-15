package com.denmiagkov.meter.infrastructure.in.validator.exception;
/**
 * Исключение, выбрасываемое при вводе невалидного единого пароля администратора при регистрации нового администратора
 * */
public class UserUnauthorizedException extends RuntimeException{
    public UserUnauthorizedException(){
        super("Permission denied!");
    }
}
