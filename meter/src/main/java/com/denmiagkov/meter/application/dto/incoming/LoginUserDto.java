package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

/**
 * Входяще ДТО для аутентификации пользователя
 */
@Getter
@Setter
public class LoginUserDto extends IncomingDto {
    /**
     * id пользователя
     */
    private int userId;
    /**
     * Тип действия пользоваетеля
     */
    private ActionType action;
    /**
     * Роль пользователя
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

    public LoginUserDto() {
        this.action = ActionType.AUTHENTICATION;
    }

    @Override
    public ActionType getAction() {
        return action;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }
}
