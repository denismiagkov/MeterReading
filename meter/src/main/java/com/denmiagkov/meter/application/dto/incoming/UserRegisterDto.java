package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserRole;

/**
 * Входящее ДТО для регистрации пользователя
 */
public class UserRegisterDto extends IncomingDto {
    /**
     * id пользователя
     */
    private int userId;
    /**
     * ТИп действия пользователя
     */
    private ActionType action;
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * Телефон пользователя
     */
    private String phone;
    /**
     * Адрес пользователя
     */
    private String address;
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
     * Регистрационный  пароль администратора
     */
    private String adminPassword;

    /**
     * Конструкторы, геттеры и сеттеры
     */
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
