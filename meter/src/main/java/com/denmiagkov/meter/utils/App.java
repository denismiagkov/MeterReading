package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.application.repository.Storage;
import com.denmiagkov.meter.application.service.ReadingServiceImpl;
import com.denmiagkov.meter.application.service.UserServiceImpl;
import com.denmiagkov.meter.infrastructure.in.Console;
import com.denmiagkov.meter.infrastructure.in.Controller;

public class App {
    public static void init(){
        Storage storage = new Storage();
        UserServiceImpl userService = new UserServiceImpl(storage);
        ReadingServiceImpl readingService = new ReadingServiceImpl(storage);
        Controller controller = new Controller(userService, readingService);
        Console console = new Console(controller);

        console.start();
    }
}
