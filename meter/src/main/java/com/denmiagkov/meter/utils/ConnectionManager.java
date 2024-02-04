package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.application.exception.DatabaseConnectionNotEstablishedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectionManager {
    private static final String URL_KEY = "datasource.url";
    private static final String USERNAME_KEY = "datasource.username";
    private static final String PASSWORD_KEY = "datasource.password";

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new DatabaseConnectionNotEstablishedException(e.getMessage());
        }
    }
}
