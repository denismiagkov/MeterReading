package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.UserRole;
import lombok.Builder;
import lombok.Value;

@Value
public class UserDto {
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
     * Роль
     */
    UserRole role;
    /**
     * Логин пользователя
     */
    String login;
}
