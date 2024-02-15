package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.utils.IncomingDtoHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер
 */
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
    @PostMapping(value = "/reading/new", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MeterReadingDto submitNewMeterReading(@RequestHeader("Authorization") String header,
                                                 @RequestBody MeterReadingSubmitDto requestDto) {
        String token = authService.verifyUser(header);
        dtoHandler.updateNewMeterReadingSubmitDto(requestDto, token);
        return meterReadingService.submitNewMeterReading(requestDto);
    }

    /**
     * Метод получения актуального  показания счетчика конкретного пользователя по указанной услуге
     *
     * @param requestDto Входящее ДТО просмотра текущих показаний счетчика
     * @return Reading Актуальное показание счетчика
     */
    @PostMapping("/reading/actual")
    public MeterReadingDto getActualReadingOnExactUtilityByUser(@RequestHeader("Authorization") String header,
                                                                @RequestBody MeterReadingReviewActualDto requestDto) {
        String token = authService.verifyUser(header);
        dtoHandler.updateMeterReadingReviewOnConcreteUtilityDto(requestDto, token);
        return meterReadingService.getActualMeterReadingOnExactUtilityByUser(requestDto);
    }

    /**
     * Метод получения всех актуальных (последних переданноых) показаний счетчиков конкретного пользователя
     *
     * @param requestDto Входящее ДТО просмотра текущих показаний счетчика
     * @return List<MeterReadingDto> Список актуальных показаний счетчика
     */
    @PostMapping("/readings/actual")
    public List<MeterReadingDto> getActualMeterReadingsOnAllUtilitiesByUser(@RequestHeader("Authorization") String header) {
        String token = authService.verifyUser(header);
        MeterReadingReviewActualDto requestDto = dtoHandler.createMeterReadingReviewAllActualDto(token);
        return meterReadingService.getActualMeterReadingsOnAllUtilitiesByUser(requestDto);
    }

    /**
     * Метод просмотра истории подачи показаний счетчиков конкретным пользователем с учетом параметра пагинации
     *
     * @param requestDto Входящее ДТО просмотра истории передачи показаний
     * @param pageSize   Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметра пагинации
     */
    @PostMapping(value = "/readings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MeterReadingDto> getMeterReadingsHistoryByUser(@RequestHeader("Authorization") String header,
                                                               @RequestBody MeterReadingReviewHistoryDto requestDto) {
        String token = authService.verifyUser(header);
        dtoHandler.createMeterReadingReviewHistoryDto(requestDto, token);
        return meterReadingService.getMeterReadingsHistoryByUser(requestDto);
    }

    /**
     * Метод просмотра показаний, переданных конкретным пользователем, в конкретном месяце
     *
     * @param requestDto Входящее ДТО просмотра показаний счетчика за выбранный месяц
     * @return List<MeterReading> Список показаний счетчиков
     */
    @PostMapping("/readings/month")
    public List<MeterReadingDto> getReadingsForMonthByUser(@RequestHeader("Authorization") String header,
                                                           @RequestBody MeterReadingReviewForMonthDto requestDto) {
        String token = authService.verifyUser(header);
        dtoHandler.updateMeterReadingReviewForMonthDto(requestDto, token);
        return meterReadingService.getReadingsForMonthByUser(requestDto);
    }
}
