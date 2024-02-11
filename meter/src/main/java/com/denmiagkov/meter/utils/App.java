package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.application.repository.*;
import com.denmiagkov.meter.application.service.*;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import liquibase.exception.LiquibaseException;

import java.sql.SQLException;

public class App {
    public static void init() throws SQLException, LiquibaseException {
        try (var connection = ConnectionManager.open()
        ) {
            var liquibase = LiquibaseManager.startLiquibase();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
