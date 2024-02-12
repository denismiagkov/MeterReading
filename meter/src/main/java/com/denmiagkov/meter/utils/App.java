package com.denmiagkov.meter.utils;

import liquibase.exception.LiquibaseException;

import java.sql.SQLException;

/**
 * Метод старта приложения
 */
public class App {
    public static void init() throws SQLException, LiquibaseException {
        try (var connection = ConnectionManager.open()
        ) {
            LiquibaseManager.startLiquibase();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
