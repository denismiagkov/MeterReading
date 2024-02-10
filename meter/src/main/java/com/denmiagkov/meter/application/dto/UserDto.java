package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

@Value
public class UserDto implements Serializable {
    /**
     * id пользователя
     */
    int id;
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
}
