package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.config.yaml.LiquibaseConfig;
import com.denmiagkov.meter.utils.exceptions.DatabaseConnectionNotEstablishedException;
import com.denmiagkov.meter.utils.yaml_config.YamlUtil;
import jakarta.annotation.PostConstruct;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public final class LiquibaseManager {
    private static final String QUERY_CREATE_MIGRATION_SCHEMA = "CREATE SCHEMA IF NOT EXISTS";
    private final LiquibaseConfig config;

    @Autowired
    private LiquibaseManager(LiquibaseConfig liquibaseConfig) {
        config = liquibaseConfig;
    }

    @PostConstruct
    void init() {
        String template = String.join(" ",
                QUERY_CREATE_MIGRATION_SCHEMA,
                config.getLiquibaseSchema());
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(template)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionNotEstablishedException(e.getMessage());
        }
    }
}
