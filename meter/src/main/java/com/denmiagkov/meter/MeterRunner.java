package com.denmiagkov.meter;

import com.denmiagkov.meter.application.repository.Storage;
import com.denmiagkov.meter.application.service.ReadingService;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.infrastructure.in.Console;
import com.denmiagkov.meter.infrastructure.in.Controller;

public class MeterRunner {
    public static void main(String[] args) {
        Storage storage = new Storage();
        UserService userService = new UserService(storage);
        ReadingService readingService = new ReadingService(storage);
        Controller controller = new Controller(userService, readingService);
        Console console = new Console(controller);

        console.start();
    }
}
