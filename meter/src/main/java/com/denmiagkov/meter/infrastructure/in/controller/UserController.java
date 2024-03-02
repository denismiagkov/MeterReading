package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.infrastructure.in.dto_handling.IncomingDtoBuilder;
import com.denmiagkov.starter.logging.aspect.annotations.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер, обрабатывающий обращения пользователя
 */
@Tag(name = "User")
@Loggable
@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    public static final String TOKEN_ATTRIBUTE_NAME = "token";
    /**
     * Сервис подачи показаний
     */
    private final MeterReadingService meterReadingService;
    /**
     * Создатель входящих ДТО
     */
    private final IncomingDtoBuilder dtoHandler;

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
            @RequestAttribute(TOKEN_ATTRIBUTE_NAME) @Parameter(description = "JWT token") String token,
            @RequestBody @Parameter(description = "user input data") SubmitNewMeterReadingDto requestDto) {
        dtoHandler.updateSubmitNewMeterReadingDto(requestDto, token);
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
    @GetMapping(value = "/reading/actual", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MeterReadingDto> getActualReadingOnExactUtilityByUser(
            @RequestAttribute(TOKEN_ATTRIBUTE_NAME) @Parameter(description = "JWT token") String token,
            @RequestParam(name = "utilityId") @Parameter(description = "includes utility type (id)") int utilityId) {
        ReviewActualMeterReadingDto requestDto = dtoHandler.createReviewMeterReadingOnConcreteUtilityDto(utilityId, token);
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
    @GetMapping(value = "/readings/actual", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterReadingDto>> getActualMeterReadingsOnAllUtilitiesByUser(
            @RequestAttribute(TOKEN_ATTRIBUTE_NAME) @Parameter(description = "JWT token") String token) {
        ReviewActualMeterReadingDto requestDto = dtoHandler.createReviewAllActualMeterReadingsDto(token);
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
    @GetMapping(value = "/readings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterReadingDto>> getMeterReadingsHistoryByUser(
            @RequestAttribute(TOKEN_ATTRIBUTE_NAME) @Parameter(description = "JWT token") String token,
            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "parameter of pagination - page") int page,
            @RequestParam(name = "size", defaultValue = "50") @Parameter(description = "parameter of pagination - page size") int size) {
        Pageable pageable = Pageable.of(page, size);
        ReviewMeterReadingHistoryDto requestDto = dtoHandler.createMeterReadingReviewHistoryDto(token);
        List<MeterReadingDto> historySubmittingMeterReadingsByUser = meterReadingService.getMeterReadingsHistoryByUser(requestDto, pageable);
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
    @GetMapping(value = "/readings/month", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterReadingDto>> getReadingsForMonthByUser(
            @RequestAttribute(TOKEN_ATTRIBUTE_NAME) @Parameter(description = "JWT token") String token,
            @RequestParam(name = "month") @Parameter(description = "month") int month,
            @RequestParam(name = "year") @Parameter(description = "year") int year) {
        ReviewMeterReadingForMonthDto requestDto = dtoHandler.createReviewMeterReadingsForMonthDto(month, year, token);
        List<MeterReadingDto> listMeterReadingsOnSelectedMonthByUser = meterReadingService.getReadingsForMonthByUser(requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listMeterReadingsOnSelectedMonthByUser);
    }
}
