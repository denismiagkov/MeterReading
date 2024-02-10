package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.application.dto.UserActionDto;
import com.denmiagkov.meter.application.dto.UserIncomingDto;
import com.denmiagkov.meter.application.dto.UserDto;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.application.service.UserService;
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
     * //     *
     * //     * @param name     Имя пользоыателя
     * //     * @param phone    Телефон пользователя
     * //     * @param address  Адрес пользователя
     * //     * @param login    Логин пользователя
     * //     * @param password Пароль пользователя
     *
     * @return User
     */
    public UserDto registerUser(UserIncomingDto userDto) {
        return userService.registerUser(userDto);
    }

    /**
     * Метод возвращает множество всех пользователей
     *
     * @return Set<User> Множество зарегистрированных пользователей
     */
    public Set<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Метод получения всех показаний всех пользователей с учетом параметров пагинации
     *
     * @param pageSize Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметра пагинации
     */
    public List<List<MeterReadingDto>> getAllReadingsList(int pageSize) {
        return meterReadingService.getAllReadingsList(pageSize);
    }

    /**
     * Метод возвращает список всех действий, совершенных пользователями
     *
     * @return List<Activity>
     */
    public List<List<UserActionDto>> getUserActivitiesList(int pageSize) {
        return activityService.getUserActivitiesList(pageSize);
    }

    /**
     * Метод добавляет новый тип услуг (расширяет перечень подаваемых показаний)
     *
     * @param utilityName новый тип подаваемых показаний
     */
    public Map<Integer, String> addUtilityTypeToDictionary(String utilityName) {
       return dictionaryService.addUtilityTypeToDictionary(utilityName);
    }

    /**
     * Метод передачи показания счетчика
     *
     * @param user         Пользователь
     * @param meterReading Показание счетчика
     */
    public void submitNewMeterReading(MeterReadingDto meterReading) {
        meterReadingService.submitNewMeterReading(meterReading);
    }

    /**
     * Метод получения актуального  показания счетчика конкретного пользователя по указанной услуге
     *
     * @param user      Пользователь
     * @param utilityId id типа услуг
     * @return Reading Актуальное показание счетчика
     */
    public MeterReadingDto getActualReadingOnExactUtilityByUser(MeterReadingDto meterReadingDto) {
        return meterReadingService.getActualMeterReadingOnExactUtilityByUser(meterReadingDto);
    }

    /**
     * Метод получения всех актуальных (последних переданноых) показаний счетчиков конкретного пользователя
     *
     * @param user Пользователь
     * @return List<MeterReading> Список актуальных показаний счетчика
     */
    public List<MeterReadingDto> getActualMeterReadingsOnAllUtilitiesByUser(int userId) {
        return meterReadingService.getActualMeterReadingsOnAllUtilitiesByUser(userId);
    }

    /**
     * Метод просмотра истории подачи показаний счетчиков конкретным пользователем с учетом параметра пагинации
     *
     * @param user     Пользователь
     * @param pageSize Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметра пагинации
     */
    public List<List<MeterReadingDto>> getMeterReadingsHistoryByUser(int userId, int pageSize) {
        return meterReadingService.getMeterReadingsHistoryByUser(userId, pageSize);
    }

    /**
     * Метод просмотра показаний, переданных конкретным пользователем, в конкретном месяце
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     * @return List<MeterReading> Список показаний счетчиков
     */
    public List<MeterReadingDto> getReadingsForMonthByUser(int userId, Map<String, Integer> month) {
        return meterReadingService.getReadingsForMonthByUser(userId, month);
    }

    /**
     * Метод фиксирует выход пользователя из приложения
     *
     * @param user Пользователь
     */
//    public void recordExit(OutcomingUserDto user) {
//        userService.recordExit(user);
//    }

    /**
     * Метод получения справочника показаний
     *
     * @return Map<Integer, String> Справочник показаний (типов услуг)
     */
    public Map<Integer, String> getUtilitiesDictionary() {
        return dictionaryService.getUtilitiesDictionary();
    }
}
