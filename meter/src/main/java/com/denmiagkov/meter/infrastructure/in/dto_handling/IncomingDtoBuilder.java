package com.denmiagkov.meter.infrastructure.in.dto_handling;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.validatorImpl.MeterReadingDtoValidatorImpl;
import com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.validatorImpl.UserIncomingDtoValidatorImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Класс реализует создание и валидацию ДТО на основе входящих данных
 */
@Component
@AllArgsConstructor
public class IncomingDtoBuilder {

    private final AuthService authService;
    private final MeterReadingDtoValidatorImpl meterReadingValidator;
    private final UserIncomingDtoValidatorImpl userIncomingDtoValidator;

    /**
     * Обновление и валидация входящего ДТО передачи нового показания счетчика
     *
     * @param meterReading Входящее ДТО с новыми показаниями счетчика
     * @param token        JWT-token
     */
    public void updateSubmitNewMeterReadingDto(SubmitNewMeterReadingDto meterReading, String token) {
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        meterReading.setDate(LocalDateTime.now());
        meterReadingValidator.isValid(meterReading);
    }

    /**
     * Создание и валидация входящего ДТО просмотра показаний счетчика за выбранный месяц
     *
     * @param month Месяц
     * @param year  Год
     * @param token JWT-token
     * @return Входящее ДТО
     */
    public ReviewMeterReadingForMonthDto createReviewMeterReadingsForMonthDto(int month, int year, String token) {
        meterReadingValidator.isValidMonth(month, year);
        ReviewMeterReadingForMonthDto requestDto = new ReviewMeterReadingForMonthDto();
        int userId = authService.getUserIdFromToken(token);
        requestDto.setUserId(userId);
        requestDto.setMonth(month);
        requestDto.setYear(year);
        return requestDto;
    }

    /**
     * Создание и валидация входящего ДТО просмотра истории передачи показаний счетчика пользователем
     *
     * @param page     Параметр пагинации - номер страницы
     * @param pageSize Параметр пагинации - размер страницы
     * @param token    JWT-token
     * @return Входящее ДТО
     */
    public ReviewMeterReadingHistoryDto createMeterReadingReviewHistoryDto(String token) {
        ReviewMeterReadingHistoryDto requestDto = new ReviewMeterReadingHistoryDto();
        int userId = authService.getUserIdFromToken(token);
        requestDto.setUserId(userId);
        return requestDto;
    }

    /**
     * Создание и валидация входящего ДТО просмотра всех актуальных показаний счетчиков пользователя
     *
     * @param token JWT-token
     * @return Входящее ДТО
     */
    public ReviewActualMeterReadingDto createReviewAllActualMeterReadingsDto(String token) {
        int userId = authService.getUserIdFromToken(token);
        ReviewActualMeterReadingDto requestDto = new ReviewActualMeterReadingDto();
        requestDto.setUserId(userId);
        return requestDto;
    }

    /**
     * Создание и валидация входящего ДТО просмотра истории передачи показаний счетчика пользователем
     *
     * @param utilityId Идентификатор типа услуг
     * @param token     JWT-token
     * @return Входящее ДТО
     */
    public ReviewActualMeterReadingDto createReviewMeterReadingOnConcreteUtilityDto(int utilityId, String token) {
        ReviewActualMeterReadingDto requestDto = new ReviewActualMeterReadingDto();
        requestDto.setUtilityId(utilityId);
        meterReadingValidator.isValidMeterReadingUtilityType(requestDto);
        int userId = authService.getUserIdFromToken(token);
        requestDto.setUserId(userId);
        return requestDto;
    }

    /**
     * Создание и валидация входящего ДТО регистрации нового пользователя
     *
     * @param userIncomingDto Входящее ДТО
     */
    public void verifyRegisterUserDto(RegisterUserDto userIncomingDto) {
        userIncomingDtoValidator.isValid(userIncomingDto);
    }
}
