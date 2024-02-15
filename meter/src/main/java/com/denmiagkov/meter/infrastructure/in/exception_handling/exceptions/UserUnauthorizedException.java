package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;
/**
 * Исключение, выбрасываемое при вводе невалидного единого пароля администратора при регистрации нового администратора
 * */
public class UserUnauthorizedException extends ValidationException{
    public UserUnauthorizedException(){
        super("Permission denied!");
    }
}
