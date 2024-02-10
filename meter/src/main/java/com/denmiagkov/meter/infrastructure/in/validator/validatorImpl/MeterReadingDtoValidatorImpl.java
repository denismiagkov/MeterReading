package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.dto.MeterReadingSubmitDto;
import com.denmiagkov.meter.infrastructure.in.validator.exception.NewMeterValueIsLessThenPreviousException;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.validator.DtoValidator;
import com.denmiagkov.meter.infrastructure.in.validator.exception.InvalidDateException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.SubmitReadingOnTheSameMonthException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.UtilityTypeNotFoundException;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Класс, валидирующий данные о новом показании счетчика, введенные пользователем
 */

public class MeterReadingDtoValidatorImpl implements DtoValidator<MeterReadingSubmitDto> {
    /**
     * Контроллер
     */
    Controller controller;

    public MeterReadingDtoValidatorImpl(Controller controller) {
        this.controller = controller;
    }

    /**
     * Метод проверки передачи корректного id типа услуг (показаний)
     *
     * @param newMeterReading Новое показание счетчика
     * @return boolean возвращает true - если условие выполняется, false - если нет
     */
    public boolean isValidMeterReadingUtilityType(MeterReadingSubmitDto newMeterReading) {
        int utilityCount = controller.getUtilitiesDictionary().size();
        if (newMeterReading.getUtilityId() <= utilityCount
            && newMeterReading.getUtilityId() > 0) {
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
    public boolean isValidDate(MeterReadingSubmitDto newMeterReading) {
        MeterReadingSubmitDto actualMeterReadingSubmitDto =
                controller.getActualReadingOnExactUtilityByUser(newMeterReading);
        LocalDateTime now = newMeterReading.getDate();
        if (actualMeterReadingSubmitDto == null
            || (actualMeterReadingSubmitDto.getDate().getMonthValue() != now.getMonthValue()
                || actualMeterReadingSubmitDto.getDate().getYear() == now.getYear())) {
            return true;
        } else {
            throw new SubmitReadingOnTheSameMonthException();
        }
    }

    /**
     * Метод проверки, исключающий возможность внесения показаний, меньших актуальных значений по величине
     *
     * @param newMeterReading Новое показание счетчика
     * @return boolean возвращает true - если условие выполняется, false - если нет
     */
    public boolean isValidMeterValue(MeterReadingSubmitDto newMeterReading) {
        MeterReadingSubmitDto actualMeterReading = controller.getActualReadingOnExactUtilityByUser(newMeterReading);
        if (actualMeterReading == null
            || (actualMeterReading.getValue() <= newMeterReading.getValue()
                && newMeterReading.getValue() >= 0)) {
            return true;
        } else {
            throw new NewMeterValueIsLessThenPreviousException();
        }
    }

    @Override
    public boolean isValid(MeterReadingSubmitDto meterReading) {
        return isValidMeterReadingUtilityType(meterReading)
               && isValidDate(meterReading)
               && isValidMeterValue(meterReading);
    }

    public boolean isValidMonth(Map<String, Integer> requestMonth) {
        if (
                (requestMonth.get("month") >= 1)
                && (requestMonth.get("month") <= 12)
                && (requestMonth.get("year") >= 2000)
                && (requestMonth.get("year") <= 2024)
        ) {
            return true;
        } else {
            throw new InvalidDateException();
        }
    }
}
