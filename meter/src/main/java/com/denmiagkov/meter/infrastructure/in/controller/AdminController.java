package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.incoming.PaginationDto;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.outgoing.UserActionDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Контроллер
 */
@Loggable
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
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
    private final AuthService authService;

    /**
     * Конструктор
     */
    @Autowired
    public AdminController(UserService userService,
                           MeterReadingService meterReadingService,
                           UserActivityService activityService,
                           DictionaryService dictionaryService,
                           AuthService authService) {
        this.userService = userService;
        this.meterReadingService = meterReadingService;
        this.activityService = activityService;
        this.dictionaryService = dictionaryService;
        this.authService = authService;
    }

    /**
     * Метод возвращает множество всех пользователей
     *
     * @return Set<User> Множество зарегистрированных пользователей
     */
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Set<UserDto> getAllUsers(@RequestHeader("Authorization") String header,
                                    @RequestBody PaginationDto paginationDto) {
        authService.verifyAdmin(header);
        return userService.getAllUsers(paginationDto);
    }

    /**
     * Метод получения всех показаний всех пользователей с учетом параметров пагинации
     *
     * @param paginationParam Параметры пагинации
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметра пагинации
     */
    @PostMapping(value = "/readings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MeterReadingDto> getAllReadingsList(@RequestHeader("Authorization") String header,
                                                    @RequestBody PaginationDto paginationParam) {
        authService.verifyAdmin(header);
        return meterReadingService.getAllReadingsList(paginationParam);
    }

    /**
     * Метод возвращает список всех действий, совершенных пользователями
     *
     * @return List<Activity>
     */
    @PostMapping(value = "/actions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<UserActionDto> getUserActivitiesList(@RequestHeader("Authorization") String header,
                                                     @RequestBody PaginationDto paginationParam) {
        authService.verifyAdmin(header);
        return activityService.getUserActivitiesList(paginationParam);
    }

    /**
     * Метод добавляет новый тип услуг (расширяет перечень подаваемых показаний)
     *
     * @param utilityName новый тип подаваемых показаний
     */
    @PostMapping("/dictionary/new")
    public Map<Integer, String> addUtilityTypeToDictionary(String utilityName) {
        return dictionaryService.addUtilityTypeToDictionary(utilityName);
    }
}
