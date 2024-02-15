package com.denmiagkov.meter.domain;

import com.denmiagkov.meter.application.service.exception.AdminNotAuthorizedException;

/**
 * Класс пользователя. Включает обычного пользователя и администратора
 */
public class User {
    /**
     * Пароль администратора (необходим для регистрации нового администратора)
     */
    private static final String ADMIN_PASSWORD = "123admin";
    /**
     * Уникальный идентификатор пользователя
     */
    private int id;
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
     * Роль
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

    public User() {
    }

    /**
     * Конструктор обычного пользователя
     */
    public User(String name, String phone, String address, String login, String password) {
        createUser(name, phone, address, login, password);
        this.role = UserRole.USER;
    }

    /**
     * Конструктор администратора
     *
     * @throws AdminNotAuthorizedException в случае ввода некорректного пароля
     */
    public User(String name, String phone, String address, String login, String password,
                String isAdmin, String adminPassword) {
        if (isAdmin.equalsIgnoreCase(String.valueOf(UserRole.ADMIN)) &&
            adminPassword.equals(ADMIN_PASSWORD)) {
            createUser(name, phone, address, login, password);
            this.role = UserRole.ADMIN;
        } else {
            throw new AdminNotAuthorizedException();
        }
    }

    /**
     * Метод создания пользователя без учета роли user / admin
     *
     * @param name     Имя
     * @param phone    Телефон
     * @param address  Адрес
     * @param login    Логин
     * @param password Пароль
     */
    private void createUser(String name, String phone, String address, String login, String password) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.login = login;
        this.password = password;
    }

    public User(int id, String name, String phone, String address, UserRole role, String login, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.login = login;
        this.password = password;
    }

    /**
     * Геттеры, сеттеры, методы equals, hashcode
     * */
    public String getAdminPassword() {
        return ADMIN_PASSWORD;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
