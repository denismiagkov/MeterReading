package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.starter.audit.domain.ActionType;
import com.denmiagkov.starter.audit.dto.IncomingDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Входящее ДТО для регистрации пользователя
 */
@Getter
@Setter
public class RegisterUserDto extends IncomingDto {
    private int userId;
    private ActionType action;
    private String name;
    private String phone;
    private String address;
    private UserRole role;
    private String login;
    private String password;
    private String adminPassword;

    public RegisterUserDto() {
        this.action = ActionType.REGISTRATION;
    }

    @Builder
    public RegisterUserDto(String name, String phone, String address, UserRole role, String login, String password, String adminPassword) {
        this();
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.login = login;
        this.password = password;
        this.adminPassword = adminPassword;
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
