package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.UserRole;
import lombok.Getter;
import lombok.ToString;


//@Value
@Getter
@ToString
public class UserIncomingDto {
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
     * Роль пользователя
     */
    UserRole role;
    /**
     * Логин пользователя
     */
    String login;
    /**
     * Пароль пользователя
     */
    String password;
    String adminPassword;

    public UserIncomingDto() {

    }

    public UserIncomingDto(String name, String phone, String address, UserRole role, String login, String password, String adminPassword) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.login = login;
        this.password = password;
        this.adminPassword = adminPassword;
    }
}
