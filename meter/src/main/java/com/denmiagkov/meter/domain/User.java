package com.denmiagkov.meter.domain;

import com.denmiagkov.meter.application.exception.AdminNotAuthorizedException;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Класс пользователя. Включает обычного пользователя и администратора
 */
@EqualsAndHashCode(of = {"name", "phone"})
@ToString(exclude = "ADMIN_PASSWORD")
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    /**
     * Пароль администратора (необходим для регистрации нового администратора)
     */
    final String ADMIN_PASSWORD = "123admin";
    /**
     * Уникальный идентификатор пользователя
     */
    final UUID id = UUID.randomUUID();
    /**
     * Имя пользователя
     */
    String name;
    /**
     * Телефон пользователя
     */
    String phone;
    /**
     * Адрес пользователя
     */
    String address;
    /**
     * Статус администратора
     */
    boolean isAdmin;
    /**
     * Логин пользователя
     */
    String login;
    /**
     * Пароль пользователя
     */
    String password;


    /**
     * Конструктор обычного пользователя
     */
    public User(String name, String phone, String address, String login, String password) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isAdmin = false;
        this.login = login;
        this.password = password;
    }

    /**
     * Конструктор администратора
     *
     * @throws AdminNotAuthorizedException
     */
    public User(String name, String phone, String login, String password,
                String inputIsAdmin, String adminPassword) {
        boolean isAdmin = Boolean.parseBoolean(inputIsAdmin);
        if (isAdmin && ADMIN_PASSWORD.equals(adminPassword)) {
            this.name = name;
            this.phone = phone;
            this.isAdmin = true;
            this.login = login;
            this.password = password;
        } else {
            throw new AdminNotAuthorizedException();
        }
    }
}
