package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.application.repository.*;
import com.denmiagkov.meter.application.service.*;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import liquibase.exception.LiquibaseException;

import java.sql.SQLException;

public class App {
    public static Controller init() throws SQLException, LiquibaseException {

        try (var connection = ConnectionManager.open()
        ) {
            var liquibase = LiquibaseManager.startLiquibase();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        ActivityRepository activityRepository = new ActivityRepositoryImpl();
        MeterReadingRepository meterReadingRepository = new MeterReadingRepositoryImpl();
        DictionaryRepository dictionaryRepository = new DictionaryRepositoryImpl();
        UserActivityService userActivityService = new UserActivityServiceImpl(activityRepository);
        UserServiceImpl userService = new UserServiceImpl(userRepository, userActivityService);
        MeterReadingServiceImpl readingService = new MeterReadingServiceImpl(meterReadingRepository, userActivityService);
        DictionaryService dictionaryService = new DictionaryServiceImpl(dictionaryRepository);
        Controller controller = new Controller(userService, readingService, userActivityService, dictionaryService);
        return controller;
    }
}
