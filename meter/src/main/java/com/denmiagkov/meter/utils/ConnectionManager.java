package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.config.yaml.DatasourceConfig;
import com.denmiagkov.meter.utils.exceptions.DatabaseConnectionNotEstablishedException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectionManager {

    private final DatasourceConfig config;

    public Connection open() {
        try {
            return DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            );
        } catch (SQLException e) {
            throw new DatabaseConnectionNotEstablishedException(e.getMessage());
        }
    }
}
