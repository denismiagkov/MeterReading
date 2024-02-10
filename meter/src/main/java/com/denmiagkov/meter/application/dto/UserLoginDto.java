package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.UserRole;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

@Value
public class UserLoginDto implements Serializable {
    /**
     * id пользователя
     * */
    int id;
    /**
     * Роль пользователя
     * */
    UserRole role;
    /**
     * Логин пользователя
     */
    String login;
    /**
     * Пароль пользователя
     */
    String password;


}
