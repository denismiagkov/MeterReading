package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.UserActionDto;
import com.denmiagkov.meter.application.dto.UserDto;
import com.denmiagkov.meter.application.service.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Контроллер
 */

public class Controller {
    public static final Controller INSTANCE = new Controller();
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

    public Controller() {
        this.userService = UserServiceImpl.INSTANCE;
        this.meterReadingService = MeterReadingServiceImpl.INSTANCE;
        this.activityService = UserActivityServiceImpl.INSTANCE;
        this.dictionaryService = DictionaryServiceImpl.INSTANCE;
    }

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
    public UserDto registerUser(UserRegisterDto userDto) {
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
    public MeterReadingDto submitNewMeterReading(MeterReadingSubmitDto meterReading) {
        return meterReadingService.submitNewMeterReading(meterReading);
    }

    /**
     * Метод получения актуального  показания счетчика конкретного пользователя по указанной услуге
     *
     * @param user      Пользователь
     * @param utilityId id типа услуг
     * @return Reading Актуальное показание счетчика
     */
    public MeterReadingDto getActualReadingOnExactUtilityByUser(MeterReadingReviewActualDto requestDto) {
        return meterReadingService.getActualMeterReadingOnExactUtilityByUser(requestDto);
    }

    /**
     * Метод получения всех актуальных (последних переданноых) показаний счетчиков конкретного пользователя
     *
     * @param user Пользователь
     * @return List<MeterReading> Список актуальных показаний счетчика
     */
    public List<MeterReadingDto> getActualMeterReadingsOnAllUtilitiesByUser(MeterReadingReviewActualDto requestDto) {
        return meterReadingService.getActualMeterReadingsOnAllUtilitiesByUser(requestDto);
    }

    /**
     * Метод просмотра истории подачи показаний счетчиков конкретным пользователем с учетом параметра пагинации
     *
     * @param user     Пользователь
     * @param pageSize Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметра пагинации
     */
    public List<List<MeterReadingDto>> getMeterReadingsHistoryByUser(MeterReadingReviewHistoryDto requestDto, int pageSize) {
        return meterReadingService.getMeterReadingsHistoryByUser(requestDto, pageSize);
    }

    /**
     * Метод просмотра показаний, переданных конкретным пользователем, в конкретном месяце
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     * @return List<MeterReading> Список показаний счетчиков
     */
    public List<MeterReadingDto> getReadingsForMonthByUser(MeterReadingReviewForMonthDto requestDto) {
        return meterReadingService.getReadingsForMonthByUser(requestDto);
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
