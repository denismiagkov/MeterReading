package com.denmiagkov.meter.domain;

import com.denmiagkov.meter.application.service.exceptions.AdminNotAuthorizedException;
import lombok.*;

/**
 * Класс пользователя. Включает обычного пользователя и администратора
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = {"name", "phone"})
public class User {

    /**
     * Уникальный идентификатор пользователя
     */
    private int id;

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
     * Роль
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

    @Builder
    public User(String name, String phone, String address, String login, String password) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.login = login;
        this.password = password;
        this.role = UserRole.USER;
    }
}
