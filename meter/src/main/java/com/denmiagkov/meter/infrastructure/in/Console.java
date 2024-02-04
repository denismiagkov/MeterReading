package com.denmiagkov.meter.infrastructure.in;

import com.denmiagkov.meter.application.exception.SubmitReadingOnTheSameMonthException;
import com.denmiagkov.meter.application.exception.NewMeterValueIsLessThenPreviousException;
import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.domain.UserRole;

import java.time.LocalDateTime;
import java.util.*;

import static com.denmiagkov.meter.application.service.DictionaryServiceImpl.PUBLIC_UTILITIES_LIST;
import static com.denmiagkov.meter.infrastructure.in.Console.ConsoleValidator.checkMonth;
import static com.denmiagkov.meter.infrastructure.in.Console.ConsoleValidator.checkPreviousMeterValue;


/**
 * Класс консоль
 */
public class Console {
    /**
     * Контроллер
     */
    private final Controller controller;
    /**
     * Сканер
     */
    private final Scanner scanner;
    /**
     * Пользователь
     */
    private User user;

    /**
     * Конструктор
     */
    public Console(Controller controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Метод открытия стартового меню приложения
     */
    public void start() {
        while (true) {
            System.out.println("================================================================");
            System.out.println("0 - Завершение работы приложения");
            System.out.println("1 - Регистрация пользователя");
            System.out.println("2 - Регистрация администратора");
            System.out.println("3 - Вход в систему");
            System.out.println("Выберите пункт меню: ");

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 0 -> {
                        System.out.println("Завершение работы приложения.");
                        return;
                    }
                    case 1 -> registerUser();
                    case 2 -> registerAdmin();
                    case 3 -> authorizeUser();
                }

            } else {
                System.out.println("Некорректный пункт меню.\nПожалуйста, повторите попытку ввода.");
                scanner.nextLine();
            }
        }

    }

    /**
     * Метод открытия окна регистрации обычного пользователя
     */
    private void registerUser() {
        System.out.println("================================================================");
        System.out.println("Введите имя пользователя:");
        String name = scanner.nextLine();
        System.out.println("Введите телефон пользователя:");
        String phone = scanner.nextLine();
        System.out.println("Введите адрес пользователя:");
        String address = scanner.nextLine();
        System.out.println("Введите логин пользователя:");
        String login = scanner.nextLine();
        System.out.println("Введите пароль пользователя:");
        String password = scanner.nextLine();

        try {
            controller.registerUser(name, phone, address, login, password);
            System.out.println("Пользователь успешно зарегистрирован");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод открытия окна регистрации администратора
     */
    private void registerAdmin() {
        System.out.println("================================================================");
        System.out.println("Введите имя администратора:");
        String name = scanner.nextLine();
        System.out.println("Введите телефон администратора:");
        String phone = scanner.nextLine();
        System.out.println("Введите адрес администратора:");
        String address = scanner.nextLine();
        System.out.println("Введите логин администратора:");
        String login = scanner.nextLine();
        System.out.println("Введите пароль администратора:");
        String password = scanner.nextLine();
        System.out.println("Подтвердите статус администратора (да - \"admin\"");
        String isAdmin = scanner.nextLine();
        System.out.println("Введите код верификации:");
        String adminPassword = scanner.nextLine();

        try {
            controller.registerAdmin(name, phone, login, address, password, isAdmin, adminPassword);
            System.out.println("Пользователь успешно зарегистрирован");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод открытия окна аутентификации пользователя
     */
    private void authorizeUser() {
        System.out.println("Введите логин:");
        String login = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        try {
            user = controller.authenticate(login, password);
            if (user.getRole().equals(UserRole.ADMIN)) {
                startAdmin();
            } else {
                startUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод открытия меню администратора
     */
    private void startAdmin() {
        while (true) {
            System.out.println("================================================================");
            System.out.println("0 - Выход из профиля");
            System.out.println("1 - Показать список пользователей");
            System.out.println("2 - Показать список всех показаний");
            System.out.println("3 - Показать список действий пользователей");
            System.out.println("4 - Добавить новый тип показаний");

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 0 -> {
                        controller.recordExit(user);
                        user = null;
                        System.out.println("Выход из профиля");
                        return;
                    }
                    case 1 -> getUserList();
                    case 2 -> getOverallReadingList(1);
                    case 3 -> getActivityList();
                    case 4 -> addUtilityType();
                }

            } else {
                System.out.println("Некорректный пункт меню.\nПожалуйста, повторите попытку ввода.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Метод открытия меню пользователя
     */
    private void startUser() {
        while (true) {
            System.out.println("================================================================");
            System.out.println("0 - Выход из профиля");
            System.out.println("1 - Передать новые показания");
            System.out.println("2 - Посмотреть актуальные показания");
            System.out.println("3 - Посмотреть показания за выбранный месяц");
            System.out.println("4 - Посмотреть историю подачи показаний");

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 0 -> {
                        controller.recordExit(user);
                        user = null;
                        System.out.println("Выход из профиля");
                        return;
                    }
                    case 1 -> conveyNewReading();
                    case 2 -> getActualMeterReadingsOnAllUtilitiesByUser();
                    case 3 -> getReadingsForMonthByUser();
                    case 4 -> getReadingsHistoryReportByUser(1);
                }

            } else {
                System.out.println("Некорректный пункт меню.\nПожалуйста, повторите попытку ввода.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Метод открытия окна просмотра показаний за конкретный месяц
     */
    private void getReadingsForMonthByUser() {
        System.out.println("Введите год:");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите месяц:");
        int month = Integer.parseInt(scanner.nextLine());
        List<MeterReading> reading = controller.getReadingsForMonthByUser(user, year, month);
        reading.forEach(System.out::println);
    }

    /**
     * Метод открытия окна просмотра истории подачи показаний пользователем
     */
    private void getReadingsHistoryReportByUser(int pageSize) {
        List<List<MeterReading>> meterReadingHistory = controller.getReadingsHistoryByUser(user, pageSize);
        System.out.println("История подачи показаний пользователем " + user.getName());
        int page = 0;
        for (List<MeterReading> currentPage : meterReadingHistory) {
            System.out.println(currentPage);
            page++;
        }
    }

    /**
     * Метод открытия окна просмотра актуальных показаний счетчиков
     */
    private void getActualMeterReadingsOnAllUtilitiesByUser() {
        List<MeterReading> actualMeterReadings = controller.getActualMeterReadingsOnAllUtilitiesByUser(user);
        if (actualMeterReadings != null) {
            System.out.println("Актуальные показатели счетчиков: \n");
            for (MeterReading reading : actualMeterReadings) {
                System.out.println(PUBLIC_UTILITIES_LIST.get(reading.getUtilityId()) + ": " +
                                   reading.getValue());
            }
        } else {
            System.out.println("Показания ранее не передавались!");
        }
    }

    /**
     * Метод открытия окна подачи новых показаний
     */
    private void conveyNewReading() {
        try {
            for (Map.Entry<Integer, String> entry : PUBLIC_UTILITIES_LIST.entrySet()) {
                int utilityId = entry.getKey();
                String utilityName = entry.getValue();
                System.out.printf("Введите показания счетчика по услуге: %s : \n", utilityName);
                String input = scanner.nextLine();
                if (!input.isEmpty()) {
                    Double meterValue = Double.parseDouble(input);
                    MeterReading lastReading = controller.getActualReadingOnExactUtilityByUser(user, utilityId);
                    if (checkMonth(lastReading) && checkPreviousMeterValue(lastReading, meterValue)) {
                        MeterReading newReading = new MeterReading(user, utilityId, meterValue);
                        controller.submitNewReading(user, newReading);
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод открытия окна добавления нового типа показаний
     */
    private void addUtilityType() {
        System.out.println("Введите новый тип услуг для подачи показаний:");
        String newUtility = scanner.nextLine();
        controller.addUtilityType(newUtility);
        System.out.println("В справочник добавлен новый тип услуг. Текущий перечень показаний:\n" +
                           "" + controller.getUtilitiesDictionary());
    }

    /**
     * Метод открытия окна просмотра всех действий пользователей
     */
    private void getActivityList() {
        var activityList = controller.getUserActivitiesList();
        for (int i = 0; i < activityList.size(); i++) {
            System.out.println(i + "\t" + activityList.get(i));
        }
    }

    /**
     * Метод открытия окна просмотра всех переданных показаний всеми пользователями
     */
    private void getOverallReadingList(int pageSize) {
        var readingList = controller.getAllReadingsList(pageSize);
        int page = 0;
        for (List<MeterReading> currentPage : readingList) {
            System.out.println(currentPage);
            page++;
        }
    }

    /**
     * Метод открытия окна просмотра всех пользователей
     */
    private void getUserList() {
        var userList = controller.getAllUsers();
        for (User user : userList) {
            System.out.println(user);
        }
    }

    /**
     * Внутренний статический класс проверки корректности данных, вводимых в консоль пользователем
     */
    static class ConsoleValidator {

        /**
         * Метод проверки, исключающий возможность повторной подачи показаний в текущем месяце
         *
         * @param reading Актуальные показания счетчика
         * @return boolean возвращает true - если условие выполняется, false - если нет
         */
        public static boolean checkMonth(MeterReading reading) {
            LocalDateTime now = LocalDateTime.now();
            if (reading == null) return true;
            if (reading.getDate().getMonthValue() == now.getMonthValue()
                && reading.getDate().getYear() == now.getYear()) {
                throw new SubmitReadingOnTheSameMonthException();
            }
            return true;
        }

        /**
         * Метод проверки, исключающий возможность внесения показаний, меньших по величине актуальных значений
         *
         * @param reading Актуальные показания счетчика
         * @param value   новое значение поеазаний
         * @return boolean возвращает true - если условие выполняется, false - если нет
         */
        public static boolean checkPreviousMeterValue(MeterReading reading, Double value) {
            if (reading == null) return true;
            if (reading.getValue() > value) {
                throw new NewMeterValueIsLessThenPreviousException();
            }
            return value != 0;
        }
    }
}
