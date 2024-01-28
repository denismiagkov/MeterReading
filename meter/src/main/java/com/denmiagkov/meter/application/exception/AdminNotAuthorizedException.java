package com.denmiagkov.meter.application.exception;
/**
 * Исключение, выбрасываемое при вводе невалидного единого пароля администратора при регистрации нового администратора
 * */
public class AdminNotAuthorizedException extends RuntimeException{
    public AdminNotAuthorizedException(){
        super("Invalid data for registration as an admin!");
    }
}
