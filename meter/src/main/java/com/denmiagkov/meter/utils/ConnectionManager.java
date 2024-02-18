package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.utils.exception.DatabaseConnectionNotEstablishedException;
import com.denmiagkov.meter.utils.yaml.YamlUtil;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Component
public final class ConnectionManager {

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
                    YamlUtil.getYaml().getDatasource().getUrl(),
                    YamlUtil.getYaml().getDatasource().getUsername(),
                    YamlUtil.getYaml().getDatasource().getPassword()
            );
        } catch (SQLException e) {
            throw new DatabaseConnectionNotEstablishedException(e.getMessage());
        }
    }
}
