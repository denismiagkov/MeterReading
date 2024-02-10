package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserAction;
import com.denmiagkov.meter.domain.UserRole;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;


public class UserLoginDto extends IncomingDtoParent {
    int userId;
    ActionType action;

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
