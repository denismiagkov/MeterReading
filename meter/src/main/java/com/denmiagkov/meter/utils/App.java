package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.application.repository.MeterReadingRepository;
import com.denmiagkov.meter.application.repository.UserRepository;
import com.denmiagkov.meter.application.service.*;
import com.denmiagkov.meter.infrastructure.in.Console;
import com.denmiagkov.meter.infrastructure.in.Controller;

public class App {
    public static void init() {
        UserRepository userRepository = new UserRepository();
        ActivityRepository activityRepository = new ActivityRepository();
        MeterReadingRepository meterReadingRepository = new MeterReadingRepository();
        UserActivityService userActivityService = new UserActivityServiceImpl(activityRepository);
        UserServiceImpl userService = new UserServiceImpl(userRepository, userActivityService);
        MeterReadingServiceImpl readingService = new MeterReadingServiceImpl(meterReadingRepository, userActivityService);
        DictionaryService dictionaryService = new DictionaryServiceImpl();
        Controller controller = new Controller(userService, readingService, userActivityService, dictionaryService);
        Console console = new Console(controller);

        console.start();
    }
}
