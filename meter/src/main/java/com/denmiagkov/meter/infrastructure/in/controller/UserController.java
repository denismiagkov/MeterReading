package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.utils.IncomingDtoHandler;
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

/**
 * Контроллер, обрабатывающий обращения пользователя
 */
@Api(tags = "User")
@Loggable
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    /**
     * Сервис подачи показаний
     */
    private final MeterReadingService meterReadingService;
    /**
     * Сервис аутентификации
     */
    private final AuthService authService;
    /**
     * Создатель входящих ДТО
     */
    private final IncomingDtoHandler dtoHandler;

    @Autowired
    public UserController(MeterReadingService meterReadingService,
                          AuthService authService,
                          IncomingDtoHandler dtoHandler) {
        this.meterReadingService = meterReadingService;
        this.authService = authService;
        this.dtoHandler = dtoHandler;
    }

    /**
     * Метод передачи показания счетчика
     *
     * @param requestDto Входящее ДТО передачи нового показания счетчика
     */
    @Operation(
            summary = "Submit new meter reading",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK - Meter reading submitted successfully",
                            content = @Content(
                                    schema = @Schema(implementation = MeterReadingDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BadRequest - User entered invalid data")
            })
    @PostMapping(value = "/reading/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MeterReadingDto> submitNewMeterReading(
            @RequestHeader("Authorization") @Parameter(description = "HTTP-header") String header,
            @RequestBody @Parameter(description = "user input data") MeterReadingSubmitDto requestDto) {
        String token = authService.verifyUser(header);
        dtoHandler.updateNewMeterReadingSubmitDto(requestDto, token);
        MeterReadingDto newSubmittedMeterReading = meterReadingService.submitNewMeterReading(requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newSubmittedMeterReading);
    }

    /**
     * Метод получения актуального  показания счетчика конкретного пользователя по указанной услуге
     *
     * @param requestDto Входящее ДТО просмотра текущих показаний счетчика
     * @return Reading Актуальное показание счетчика
     */
    @Operation(
            summary = "Shows actual meter reading on single utility selected by user",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK - Actual meter reading retrieved",
                            content = @Content(
                                    schema = @Schema(implementation = MeterReadingDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BadRequest - User entered wrong utility type")
            })
    @PostMapping(value = "/reading/actual", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MeterReadingDto> getActualReadingOnExactUtilityByUser(
            @RequestHeader("Authorization") @Parameter(description = "HTTP-header") String header,
            @RequestBody @Parameter(description = "includes utility type (id)") MeterReadingReviewActualDto requestDto) {
        String token = authService.verifyUser(header);
        dtoHandler.updateMeterReadingReviewOnConcreteUtilityDto(requestDto, token);
        MeterReadingDto actualMeterReading = meterReadingService.getActualMeterReadingOnExactUtilityByUser(requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actualMeterReading);
    }

    /**
     * Метод получения всех актуальных (последних переданноых) показаний счетчиков конкретного пользователя
     *
     * @param requestDto Входящее ДТО просмотра текущих показаний счетчика
     * @return List<MeterReadingDto> Список актуальных показаний счетчика
     */

    @Operation(
            summary = "Shows actual meter readings on all utilities, submitted by user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK - List of actual meter readings retrieved",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MeterReadingDto.class))))
            })
    @PostMapping(value = "/readings/actual", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterReadingDto>> getActualMeterReadingsOnAllUtilitiesByUser(
            @RequestHeader("Authorization") @Parameter(description = "HTTP-header") String header) {
        String token = authService.verifyUser(header);
        MeterReadingReviewActualDto requestDto = dtoHandler.createMeterReadingReviewAllActualDto(token);
        List<MeterReadingDto> listActualMeterReadings = meterReadingService.getActualMeterReadingsOnAllUtilitiesByUser(requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listActualMeterReadings);
    }

    /**
     * Метод просмотра истории подачи показаний счетчиков конкретным пользователем с учетом параметра пагинации
     *
     * @param requestDto Входящее ДТО просмотра истории передачи показаний
     * @param pageSize   Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметра пагинации
     */
    @Operation(
            summary = "Shows history of submitting meter readings by user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK - List of all user's meter readings retrieved",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MeterReadingDto.class))))
            })
    @PostMapping(value = "/readings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterReadingDto>> getMeterReadingsHistoryByUser(
            @RequestHeader("Authorization") @Parameter(description = "http-header") String header,
            @RequestBody @Parameter(description = "includes parameters of pagination") MeterReadingReviewHistoryDto requestDto) {
        String token = authService.verifyUser(header);
        dtoHandler.createMeterReadingReviewHistoryDto(requestDto, token);
        List<MeterReadingDto> historySubmittingMeterReadingsByUser = meterReadingService.getMeterReadingsHistoryByUser(requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(historySubmittingMeterReadingsByUser);
    }

    /**
     * Метод просмотра показаний, переданных конкретным пользователем, в конкретном месяце
     *
     * @param requestDto Входящее ДТО просмотра показаний счетчика за выбранный месяц
     * @return List<MeterReading> Список показаний счетчиков
     */
    @Operation(
            summary = "Shows meter readings, submitted by user on selected month",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK - List of user's meter readings on selected month retrieved",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MeterReadingDto.class)))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BadRequest - User entered wrong input data")
            })
    @PostMapping(value = "/readings/month", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterReadingDto>> getReadingsForMonthByUser(
            @RequestHeader("Authorization") @Parameter(description = "http-header") String header,
            @RequestBody @Parameter(description = "includes month, selected by user") MeterReadingReviewForMonthDto requestDto) {
        String token = authService.verifyUser(header);
        dtoHandler.updateMeterReadingReviewForMonthDto(requestDto, token);
        List<MeterReadingDto> listMeterReadingsOnSelectedMonthByUser = meterReadingService.getReadingsForMonthByUser(requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listMeterReadingsOnSelectedMonthByUser);
    }
}
