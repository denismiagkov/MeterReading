package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.outgoing.UserActionDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.aspects.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.validatorImpl.PublicUtilityValidatorImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Контроллер, обрабатывающий обращения администратора
 */
//@Api(tags = "Admin")
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
    private final PublicUtilityValidatorImpl utilityValidator;

    /**
     * Конструктор
     */
    @Autowired
    public AdminController(UserService userService,
                           MeterReadingService meterReadingService,
                           UserActivityService activityService,
                           DictionaryService dictionaryService,
                           PublicUtilityValidatorImpl utilityValidator) {
        this.userService = userService;
        this.meterReadingService = meterReadingService;
        this.activityService = activityService;
        this.dictionaryService = dictionaryService;
        this.utilityValidator = utilityValidator;
    }

    /**
     * Метод возвращает множество всех пользователей
     *
     * @return Set<User> Множество зарегистрированных пользователей
     */
    @Operation(
            summary = "Shows all registered users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK - List of all registered users",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserDto.class)))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User has no admin rights for access to requested data")
            })
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<UserDto>> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "parameter of pagination - page") int page,
            @RequestParam(name = "size", defaultValue = "50") @Parameter(description = "parameter of pagination - page size") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllUsers(page, size));
    }

    /**
     * Метод получения всех показаний всех пользователей с учетом параметров пагинации
     *
     * @param paginationParam Параметры пагинации
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметра пагинации
     */
    @Operation(
            summary = "Shows all meter readings, submitted by all users for all time",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK - List of all submitted meter readings",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MeterReadingDto.class)))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User has no admin rights for access to requested data")
            })
    @GetMapping(value = "/readings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterReadingDto>> getAllMeterReadingsList(
            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "parameter of pagination - page") int page,
            @RequestParam(name = "size", defaultValue = "50") @Parameter(description = "parameter of pagination - page size") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(meterReadingService.getAllMeterReadingsList(page, size));
    }

    /**
     * Метод возвращает список всех действий, совершенных пользователями
     *
     * @return List<Activity>
     */
    @Operation(
            summary = "Shows all actions, committed by all users in application",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK - List of all actions, committed by users",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserActionDto.class)))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User has no admin rights for access to requested data")
            })
    @GetMapping(value = "/actions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserActionDto>> getAllUsersActions(
            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "parameter of pagination - page") int page,
            @RequestParam(name = "size", defaultValue = "50") @Parameter(description = "parameter of pagination - page size") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(activityService.getUserActivitiesList(page, size));
    }

    /**
     * Метод добавляет новый тип услуг (расширяет перечень подаваемых показаний)
     *
     * @param utilityName Новый тип подаваемых показаний
     * @return Map<Integer, String> Справочник услуг
     */
    @Operation(
            summary = "Adds new kind of utilities to dictionary",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK - Utility added to dictionary succesfully",
                            content = @Content(
                                    schema = @Schema(implementation = Map.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request - This utility is already included in dictionary"),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User has no admin rights for access to requested data")
            })
    @PostMapping(value = "/dictionary/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<Integer, String>> addUtilityTypeToDictionary(
            @RequestBody @Parameter(description = "new utility name") Map<String, String> utility) {
        String newUtility = utility.get("name");
        utilityValidator.isValid(newUtility);
        Map<Integer, String> utilitiesDictionary = dictionaryService.addUtilityTypeToDictionary(newUtility);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(utilitiesDictionary);
    }
}
