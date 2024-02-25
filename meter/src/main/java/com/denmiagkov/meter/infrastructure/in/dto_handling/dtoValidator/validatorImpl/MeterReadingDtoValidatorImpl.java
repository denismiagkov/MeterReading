package com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.validatorImpl;

import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.ReviewActualMeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.SubmitNewMeterReadingDto;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.NewMeterValueIsLessThenPreviousException;
import com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.DtoValidator;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.InvalidDateException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.SubmitMeterReadingOnTheSameMonthException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.UtilityTypeNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Класс, валидирующий данные о новом показании счетчика, введенные пользователем
 */
@Component
@AllArgsConstructor
public class MeterReadingDtoValidatorImpl implements DtoValidator<SubmitNewMeterReadingDto> {

    DictionaryService dictionaryService;
    MeterReadingService meterReadingService;

    /**
     * Методы проверки передачи корректного id типа услуг (показаний)
     *
     * @param newMeterReading Новое показание счетчика
     * @return boolean возвращает true - если условие выполняется, false - если нет
     */
    public boolean isValidMeterReadingUtilityType(SubmitNewMeterReadingDto newMeterReading) {
        int utilityCount = dictionaryService.getUtilitiesDictionary().size();
        if (newMeterReading.getUtilityId() <= utilityCount) {
            return true;
        } else {
            throw new UtilityTypeNotFoundException();
        }
    }

    public boolean isValidMeterReadingUtilityType(ReviewActualMeterReadingDto newMeterReading) {
        int utilityCount = dictionaryService.getUtilitiesDictionary().size();
        if (newMeterReading.getUtilityId() <= utilityCount) {
            return true;
        } else {
            throw new UtilityTypeNotFoundException();
        }
    }

    /**
     * Метод проверки, исключающий возможность повторной подачи показаний в текущем месяце
     *
     * @param newMeterReading Новое показание счетчика
     * @return boolean возвращает true - если условие выполняется, false - если нет
     */
    public boolean isValidDate(SubmitNewMeterReadingDto newMeterReading) {
        ReviewActualMeterReadingDto requestActualMeterReading = getActualMeterReadingDto(newMeterReading);
        MeterReadingDto actualMeterReading =
                meterReadingService.getActualMeterReadingOnExactUtilityByUser(requestActualMeterReading);
        if (!isPreviousMeterReadingSubmitOnTheSameMonth(actualMeterReading, newMeterReading)) {
            return true;
        } else {
            throw new SubmitMeterReadingOnTheSameMonthException();
        }
    }

    private static boolean isPreviousMeterReadingSubmitOnTheSameMonth(
            MeterReadingDto previousMeterReading, SubmitNewMeterReadingDto newMeterReading) {
        LocalDateTime now = newMeterReading.getDate();
        return previousMeterReading != null
               && (previousMeterReading.getDate().getMonthValue() == now.getMonthValue()
                   || previousMeterReading.getDate().getYear() == now.getYear());
    }

    /**
     * Метод создает входящее ДТО для получения актуального показания счетчика пользователя по заданной услуге
     *
     * @param newMeterReading новое показание счетчика
     * @return MeterReadingReviewActualDto ДТО для получения текущего показания счетчика
     */
    private ReviewActualMeterReadingDto getActualMeterReadingDto(SubmitNewMeterReadingDto newMeterReading) {
        ReviewActualMeterReadingDto requestActualMeterReadingDto = new ReviewActualMeterReadingDto();
        requestActualMeterReadingDto.setUserId(newMeterReading.getUserId());
        requestActualMeterReadingDto.setUtilityId(newMeterReading.getUtilityId());
        return requestActualMeterReadingDto;
    }

    /**
     * Метод проверки, исключающий возможность внесения показаний, меньших актуальных значений по величине
     *
     * @param newMeterReading Новое показание счетчика
     * @return boolean возвращает true - если условие выполняется, false - если нет
     */
    public boolean isValidMeterValue(SubmitNewMeterReadingDto newMeterReading) {
        ReviewActualMeterReadingDto requestActualMeterReading = getActualMeterReadingDto(newMeterReading);
        MeterReadingDto actualMeterReading = meterReadingService.getActualMeterReadingOnExactUtilityByUser(requestActualMeterReading);
        if (isPreviousMeterReadingNotBiggerThanNew(newMeterReading, actualMeterReading)) {
            return true;
        } else {
            throw new NewMeterValueIsLessThenPreviousException();
        }
    }

    private static boolean isPreviousMeterReadingNotBiggerThanNew(SubmitNewMeterReadingDto newMeterReading, MeterReadingDto actualMeterReading) {
        return actualMeterReading == null
               || (actualMeterReading.getValue() <= newMeterReading.getValue());
    }

    /**
     * Метод проверяет корректность указания месяца для предоставления списка запрошенных показаний счечиков
     *
     * @param requestDto запрос на предоставление показаний за определенный месяц
     * @return boolean true в случае успешной проверки, в противном случае - false
     */
    public boolean isValidMonth(int month, int year) {
        if (isMonthNumberBetweenOneAndTwelve(month)
            && isPeriodNotLaterThanNow(month, year)) {
            return true;
        } else {
            throw new InvalidDateException();
        }
    }

    private static boolean isPeriodNotLaterThanNow(int month, int year) {
        return (year < LocalDateTime.now().getYear())
               || month <= LocalDateTime.now().getMonthValue()
                  && year == LocalDateTime.now().getYear();
    }

    private static boolean isMonthNumberBetweenOneAndTwelve(int month) {
        return (month >= LocalDateTime.MIN.getMonthValue())
               && (month <= LocalDateTime.MAX.getMonthValue());
    }

    /**
     * Комплексный метод валидации указанных пользователем данных о типа показаний счетчика, дате предоставления
     * сведений и значении счетчика при подаче нового покащания
     *
     * @param meterReading входящее ДТО на подачу новых показаний счетчика
     * @return boolean true в случае успешной проверки, в противном случае - false
     */
    @Override
    public boolean isValid(SubmitNewMeterReadingDto meterReading) {
        return isValidMeterReadingUtilityType(meterReading)
               && isValidDate(meterReading)
               && isValidMeterValue(meterReading);
    }
}
