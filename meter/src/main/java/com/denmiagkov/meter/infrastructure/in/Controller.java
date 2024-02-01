package com.denmiagkov.meter.infrastructure.in;

import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Класс контроллера
 */
@AllArgsConstructor
public class Controller {
    /**
     * Сервис пользователя
     */
    private final UserService userService;
    /**
     * Сервис подачи показаний
     */
    private final MeterReadingService meterReadingService;
    /**
     * Сервис действий пользователя
     */
    private final UserActivityService activityService;
    private final DictionaryService dictionaryService;

    /**
     * Метод регистрации обычного пользователя
     *
     * @param name     Имя пользоыателя
     * @param phone    Телефон пользователя
     * @param address  Адрес пользователя
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return User
     */
    public void registerUser(String name, String phone, String address, String login, String password) {
        userService.registerUser(name, phone, address, login, password);
    }

    /**
     * Метод регистрации администратора
     *
     * @param name          Имя пользоыателя
     * @param phone         Телефон пользователя
     * @param login         Логин пользователя
     * @param password      Пароль пользователя
     * @param isAdmin       Подтверждение статуса администратора
     * @param adminPassword Единый пароль администратора
     */
    public void registerAdmin(String name, String phone, String login, String password, String isAdmin, String adminPassword) {
        userService.registerUser(name, phone, login, password, isAdmin, adminPassword);
    }

    /**
     * Метод авторизации пользователя
     *
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return User
     */
    public User authorizeUser(String login, String password) {
        return userService.authorizeUser(login, password);
    }

    /**
     * Метод возвращает множество всех пользователей
     *
     * @return Set<User>
     */
    public Set<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Метод получения всех показаний всех пользователей
     *
     * @return List<Reading> Общий список показаний счетчиков
     */
    public List<List<MeterReading>> getAllReadingsList(int pageSize) {
        return meterReadingService.getAllReadingsList(pageSize);
    }

    /**
     * Метод возвращает список всех действий, совершенных пользователями
     *
     * @return List<Activity>
     */
    public List<Activity> getUserActivitiesList() {
        return activityService.getUserActivitiesList();
    }

    /**
     * Метод добавляет новый тип услуг (расширяет перечень подаваемых показаний)
     *
     * @param newUtility новый тип подаваемых показаний
     */
    public void addUtilityType(String newUtility) {
        dictionaryService.addUtilityType(newUtility);
    }

    /**
     * Метод подачи показаний
     *
     * @param user    Пользователь
     * @param reading Показания счетчиков
     */
    public void submitNewReading(User user, MeterReading reading) {
        meterReadingService.submitNewReading(user, reading);
    }

    /**
     * Метод получения актуальных (последних переданных) показаний счетчика конкретным пользователем
     *
     * @param user Пользователь
     * @return Reading Актуальные показания счетчиков
     */
    public MeterReading getActualReadingByUser(User user) {
        return meterReadingService.getActualReadingByUser(user);
    }

    /**
     * Метод просмотра истории подачи показаний
     *
     * @param user Пользователь
     * @return List<Reading> Список поданных показаний
     */
    public List<List<MeterReading>> getReadingsHistoryByUser(User user, int pageSize) {
        return meterReadingService.getReadingsHistoryByUser(user, pageSize);
    }

    /**
     * Метод просмотра показаний за конкретный месяц
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     */
    public MeterReading getReadingsForMonthByUser(User user, int year, int month) {
        return meterReadingService.getReadingsForMonthByUser(user, year, month);
    }

    /**
     * Метод фиксирует выход пользователя из приложения
     *
     * @param user Пользователь
     */
    public void recordExit(User user) {
        userService.recordExit(user);
    }


}
