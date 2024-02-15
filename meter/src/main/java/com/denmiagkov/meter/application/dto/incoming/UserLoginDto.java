package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserRole;

/**
 * Входяще ДТО для аутентификации пользователя
 */
public class UserLoginDto extends IncomingDto {
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

    /**
     * Конструктор, геттеры и сеттеры
     */
    public UserLoginDto() {
        this.action = ActionType.AUTHENTICATION;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
