package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.config.yaml.LiquibaseConfig;
import com.denmiagkov.meter.utils.exceptions.DatabaseConnectionNotEstablishedException;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
@AllArgsConstructor
public final class LiquibaseManager {
    private static final String QUERY_CREATE_MIGRATION_SCHEMA = "CREATE SCHEMA IF NOT EXISTS";
    private final LiquibaseConfig config;
    private final ConnectionManager connectionManager;

    @PostConstruct
    void init() {
        String template = String.join(" ",
                QUERY_CREATE_MIGRATION_SCHEMA,
                config.getLiquibaseSchema());
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(template)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionNotEstablishedException(e.getMessage());
        }
    }
}
