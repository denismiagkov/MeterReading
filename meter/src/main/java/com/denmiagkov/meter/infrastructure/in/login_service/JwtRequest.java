package com.denmiagkov.meter.infrastructure.in.login_service;

/**
 * Запрос на создание и выдачу токена
 */
public class JwtRequest {
    /**
     * Логин пользоваетля
     */
    private String login;
    /**
     * Пароль пользователя
     */
    private String password;

    /**
     * Конструктор, геттеры и сеттеры
     */
    public JwtRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
