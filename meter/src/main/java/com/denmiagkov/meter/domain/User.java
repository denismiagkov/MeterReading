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
@Setter
@NoArgsConstructor
@AllArgsConstructor()
@Builder
public class User {
    /**
     * Пароль администратора (необходим для регистрации нового администратора)
     */
    private final String ADMIN_PASSWORD = "123admin";
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
    @Builder
    public User(String name, String phone, String address, String login, String password) {
        createUser(name, phone, address, login, password);
        this.role = UserRole.USER;
    }

    /**
     * Конструктор администратора
     *
     * @throws AdminNotAuthorizedException в случае ввода некорректного пароля
     */
    @Builder
    public User(String name, String phone, String address, String login, String password,
                String isAdmin, String adminPassword) {
        if (isAdmin.equalsIgnoreCase(String.valueOf(UserRole.ADMIN)) &&
            adminPassword.equals(ADMIN_PASSWORD)) {
            createUser(name, phone, address, login, password);
            this.role = UserRole.ADMIN;
        } else {
            throw new AdminNotAuthorizedException();
        }
    }

    /**
     * Метод создания пользователя без учета роли user / admin
     * @param name Имя
     * @param phone Телефон
     * @param address Адрес
     * @param login Логин
     * @param password Пароль
     */
    private void createUser(String name, String phone, String address, String login, String password) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.login = login;
        this.password = password;
    }
}
