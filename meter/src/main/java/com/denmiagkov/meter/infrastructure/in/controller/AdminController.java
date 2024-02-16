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
import io.swagger.annotations.Api;
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
@Api(tags = "Admin")
@Loggable
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    public static final String HTTP_HEADER = "Authorization";
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
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<UserDto>> getAllUsers(
            @RequestHeader(HTTP_HEADER) @Parameter(description = "http=header") String header,
            @RequestBody @Parameter(description = "parameters of pagination") PaginationDto paginationDto) {
        authService.verifyAdmin(header);
        Set<UserDto> allUsers = userService.getAllUsers(paginationDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allUsers);
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
    @PostMapping(value = "/readings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterReadingDto>> getAllMeterReadingsList(
            @RequestHeader(HTTP_HEADER) @Parameter(description = "http=header") String header,
            @RequestBody @Parameter(description = "parameters of pagination") PaginationDto paginationParam) {
        authService.verifyAdmin(header);
        List<MeterReadingDto> allMeterReadings = meterReadingService.getAllReadingsList(paginationParam);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allMeterReadings);
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
    @PostMapping(value = "/actions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserActionDto>> getUserActivitiesList(
            @RequestHeader(HTTP_HEADER) @Parameter(description = "http=header") String header,
            @RequestBody @Parameter(description = "parameters of pagination") PaginationDto paginationParam) {
        authService.verifyAdmin(header);
        List<UserActionDto> overallUsersActivityList = activityService.getUserActivitiesList(paginationParam);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(overallUsersActivityList);
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
            @RequestHeader(HTTP_HEADER) @Parameter(description = "http=header") String header,
            @RequestBody @Parameter(description = "new utility name") Map<String, String> utility) {
        authService.verifyAdmin(header);
        Map<Integer, String> utilitiesDictionary = dictionaryService.addUtilityTypeToDictionary(utility.get("name"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(utilitiesDictionary);
    }
}
