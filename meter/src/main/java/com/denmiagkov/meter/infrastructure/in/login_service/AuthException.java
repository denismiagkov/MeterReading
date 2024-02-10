package com.denmiagkov.meter.infrastructure.in.login_service;

public class AuthException extends RuntimeException{
    public AuthException() {
        super("Аутентификация не пройдена! Введены неверные логин и/или пароль пользователя!");
    }
}