package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.application.exception.DatabaseConnectionNotEstablishedException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LiquibaseManager {
    private static final String SERVICE_SCHEMA_NAME_KEY = "liquibase.service_schema_name";
    private static final String DEFAULT_SCHEMA_NAME_KEY = "liquibase.default_schema_name";
    private static final String CHANGELOG_FILE_KEY = "liquibase.changelog_file";
    private static final String QUERY_CREATE_MIGRATION_SCHEMA = String.join(" ",
            "CREATE SCHEMA IF NOT EXISTS",
            PropertiesUtil.get(SERVICE_SCHEMA_NAME_KEY));

    static {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(QUERY_CREATE_MIGRATION_SCHEMA)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionNotEstablishedException(e.getMessage());
        }
    }


    public static Liquibase startLiquibase() {
        try (Connection connection = ConnectionManager.open()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(PropertiesUtil.get(SERVICE_SCHEMA_NAME_KEY));
            database.setDefaultSchemaName(PropertiesUtil.get(DEFAULT_SCHEMA_NAME_KEY));
            Liquibase liquibase = new Liquibase(PropertiesUtil.get(CHANGELOG_FILE_KEY),
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            return liquibase;
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseConnectionNotEstablishedException(e.getMessage());
        } catch (LiquibaseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}