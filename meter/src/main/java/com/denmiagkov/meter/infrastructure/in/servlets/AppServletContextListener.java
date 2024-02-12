package com.denmiagkov.meter.infrastructure.in.servlets;

import com.denmiagkov.meter.utils.App;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import liquibase.exception.LiquibaseException;

import java.sql.SQLException;

@WebListener
public class AppServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            App.init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
