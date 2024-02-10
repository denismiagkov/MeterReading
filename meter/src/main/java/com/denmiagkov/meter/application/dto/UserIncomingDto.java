package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserRole;
import lombok.Getter;
import lombok.ToString;


public class UserIncomingDto extends IncomingDtoParent{
    private int userId;
    private ActionType action;

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
        this.action = ActionType.REGISTRATION;
    }

    public UserIncomingDto(String name, String phone, String address, UserRole role, String login, String password, String adminPassword) {
        this();
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.login = login;
        this.password = password;
        this.adminPassword = adminPassword;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public UserRole getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getAdminPassword() {
        return adminPassword;
    }
}
