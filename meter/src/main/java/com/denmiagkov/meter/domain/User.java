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
public class User {
    /**
     * Пароль администратора (необходим для регистрации нового администратора)
     */
    private final String ADMIN_PASSWORD = "123admin";
    /**
     * Уникальный идентификатор пользователя
     */
    private final UUID id = UUID.randomUUID();
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * Телефон пользователя
     */
    private String phone;
    /**
     * Адрес пользователя
     */
    private String address;
    /**
     * Статус администратора
     */
    private UserRole role;
    /**
     * Логин пользователя
     */
    private String login;
    /**
     * Пароль пользователя
     */
    private String password;


    /**
     * Конструктор обычного пользователя
     */
    public User(String name, String phone, String address, String login, String password) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = UserRole.USER;
        this.login = login;
        this.password = password;
    }

    /**
     * Конструктор администратора
     *
     * @throws AdminNotAuthorizedException
     */
    public User(String name, String phone, String login, String password,
                String isAdmin, String adminPassword) {
        if (isAdmin.equalsIgnoreCase(String.valueOf(UserRole.ADMIN))
            && ADMIN_PASSWORD.equals(adminPassword)) {
            this.name = name;
            this.phone = phone;
            this.role = UserRole.ADMIN;
            this.login = login;
            this.password = password;
        } else {
            throw new AdminNotAuthorizedException();
        }
    }
}
