package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.utils.exception.DatabaseConnectionNotEstablishedException;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Component
public final class ConnectionManager {
    private static final String URL_KEY = "datasource.url";
    private static final String USERNAME_KEY = "datasource.username";
    private static final String PASSWORD_KEY = "datasource.password";

    private ConnectionManager() {
    }

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

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
