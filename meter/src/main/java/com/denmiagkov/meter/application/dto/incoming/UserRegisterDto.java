package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserRole;

/**
 * Входящее ДТО для регистрации пользователя
 */
public class UserRegisterDto extends IncomingDto {
    private int userId;
    private ActionType action;
    private String name;
    private String phone;
    private String address;
    private UserRole role;
    private String login;
    private String password;
    private String adminPassword;

    public UserRegisterDto() {
        this.action = ActionType.REGISTRATION;
    }

    public UserRegisterDto(String name, String phone, String address, UserRole role, String login, String password, String adminPassword) {
        this();
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.login = login;
        this.password = password;
        this.adminPassword = adminPassword;
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

    public void setRole(UserRole userRole) {
    }
}
