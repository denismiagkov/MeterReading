package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;


public class UserDto  {
    /**
     * id пользователя
     */
    int id;
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

    public UserDto(int id, String name, String phone, String address, UserRole role, String login) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.login = login;
    }

    public UserDto() {
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
}
