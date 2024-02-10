package com.denmiagkov.meter.infrastructure.in.login_service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class JwtRequest {
    private String login;
    private String password;

    public JwtRequest() {
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
