package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.starter.audit.dto.IncomingDto;
import lombok.Getter;
import lombok.Setter;

/**
 * Входящее ДТО для аутентификации пользователя
 */
@Getter
@Setter
public class UserLoginDto extends IncomingDto<ActionType> {
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

    public UserLoginDto() {
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
