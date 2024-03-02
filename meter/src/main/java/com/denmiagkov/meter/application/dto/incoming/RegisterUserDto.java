package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.starter.audit.dto.IncomingDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Входящее ДТО для регистрации пользователя
 */
@Getter
@Setter
public class RegisterUserDto extends IncomingDto<ActionType> {
    private int userId;
    private ActionType action;
    private String name;
    private String phone;
    private String address;
    private UserRole role;
    private String login;
    private String password;

    public RegisterUserDto() {
        this.action = ActionType.REGISTRATION;
        this.role = UserRole.USER;
    }

    @Builder
    public RegisterUserDto(String name, String phone, String address, String login, String password) {
        this();
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.login = login;
        this.password = password;
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
