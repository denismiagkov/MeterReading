package com.denmiagkov.meter.infrastructure.in.servlet;

import com.denmiagkov.meter.application.repository.ActivityRepositoryImpl;
import com.denmiagkov.meter.application.repository.UserRepositoryImpl;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.UserActivityServiceImpl;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.application.service.UserServiceImpl;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtProvider;
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
