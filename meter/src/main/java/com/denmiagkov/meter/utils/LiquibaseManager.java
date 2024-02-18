package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.utils.exceptions.DatabaseConnectionNotEstablishedException;
import com.denmiagkov.meter.utils.yaml_config.YamlUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public final class LiquibaseManager {
    private static final String QUERY_CREATE_MIGRATION_SCHEMA = String.join(" ",
            "CREATE SCHEMA IF NOT EXISTS",
            YamlUtil.getYaml().getLiquibase().getServiceSchemaName());

    private LiquibaseManager() {
    }

    static {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(QUERY_CREATE_MIGRATION_SCHEMA)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionNotEstablishedException(e.getMessage());
        }
    }

    @PostConstruct
    public static Liquibase startLiquibase() {
        try (Connection connection = ConnectionManager.open()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(YamlUtil.getYaml().getLiquibase().getServiceSchemaName());
            database.setDefaultSchemaName(YamlUtil.getYaml().getLiquibase().getDefaultSchemaName());
            Liquibase liquibase = new Liquibase(YamlUtil.getYaml().getLiquibase().getChangelogFile(),
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
