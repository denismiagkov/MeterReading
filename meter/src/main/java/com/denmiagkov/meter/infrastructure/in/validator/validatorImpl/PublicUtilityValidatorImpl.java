package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.application.exception.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.validator.DtoValidator;
import com.denmiagkov.meter.infrastructure.in.validator.exception.InvalidDateException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.NewMeterValueIsLessThenPreviousException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.SubmitReadingOnTheSameMonthException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.UtilityTypeNotFoundException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Класс, валидирующий данные о новом показании счетчика, введенные пользователем
 */
@AllArgsConstructor
public class PublicUtilityValidatorImpl implements DtoValidator<String> {
    /**
     * Контроллер
     */
    Controller controller;


    @Override
    public boolean isValid(String newUtility) {
        var utilitiesDictionary = controller.getUtilitiesDictionary();
        if (!utilitiesDictionary.containsValue(newUtility.toUpperCase())) {
            return true;
        } else {
            throw new PublicUtilityTypeAlreadyExistsException(newUtility);
        }
    }
}
