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
import java.util.Map;
import java.util.Set;

/**
 * Контроллер
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
    /**
     * Сервис справочника показаний (типов услуг)
     */
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
     * @param address       Адрес пользователя
     * @param login         Логин пользователя
     * @param password      Пароль пользователя
     * @param isAdmin       Подтверждение статуса администратора
     * @param adminPassword Единый пароль администратора
     */
    public void registerAdmin(String name, String phone, String login, String address, String password, String isAdmin, String adminPassword) {
        userService.registerUser(name, phone, address, login, password, isAdmin, adminPassword);
    }

    /**
     * Метод авторизации пользователя
     *
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return User Пользователь
     */
    public User authenticateUser(String login, String password) {
        return userService.authenticateUser(login, password);
    }

    /**
     * Метод возвращает множество всех пользователей
     *
     * @return Set<User> Множество зарегистрированных пользователей
     */
    public Set<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Метод получения всех показаний всех пользователей с учетом параметров пагинации
     *
     * @param pageSize Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметра пагинации
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
     * @param utilityName новый тип подаваемых показаний
     */
    public void addUtilityType(String utilityName) {
        dictionaryService.addUtilityTypeToDictionary(utilityName);
    }

    /**
     * Метод передачи показания счетчика
     *
     * @param user    Пользователь
     * @param reading Показание счетчика
     */
    public void submitNewReading(User user, MeterReading reading) {
        meterReadingService.submitNewReading(user, reading);
    }

    /**
     * Метод получения актуального  показания счетчика конкретного пользователя по указанной услуге
     *
     * @param user      Пользователь
     * @param utilityId id типа услуг
     * @return Reading Актуальное показание счетчика
     */
    public MeterReading getActualReadingOnExactUtilityByUser(User user, int utilityId) {
        return meterReadingService.getActualMeterReadingOnExactUtilityByUser(user, utilityId);
    }

    /**
     * Метод получения всех актуальных (последних переданноых) показаний счетчиков конкретного пользователя
     *
     * @param user Пользователь
     * @return List<MeterReading> Список актуальных показаний счетчика
     */
    public List<MeterReading> getActualMeterReadingsOnAllUtilitiesByUser(User user) {
        return meterReadingService.getActualMeterReadingsOnAllUtilitiesByUser(user);
    }

    /**
     * Метод просмотра истории подачи показаний счетчиков конкретным пользователем с учетом параметра пагинации
     *
     * @param user     Пользователь
     * @param pageSize Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметра пагинации
     */
    public List<List<MeterReading>> getReadingsHistoryByUser(User user, int pageSize) {
        return meterReadingService.getMeterReadingsHistoryByUser(user, pageSize);
    }

    /**
     * Метод просмотра показаний, переданных конкретным пользователем, в конкретном месяце
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     * @return List<MeterReading> Список показаний счетчиков
     */
    public List<MeterReading> getReadingsForMonthByUser(User user, int year, int month) {
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

    /**
     * Метод получения справочника показаний
     *
     * @return Map<Integer, String> Справочник показаний (типов услуг)
     */
    public Map<Integer, String> getUtilitiesDictionary() {
        return dictionaryService.getUtilitiesDictionary();
    }
}
