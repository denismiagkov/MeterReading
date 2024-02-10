package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.exception.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.validator.DtoValidator;

/**
 * Класс, валидирующий данные о новом показании счетчика, введенные пользователем
 */

public class PublicUtilityValidatorImpl implements DtoValidator<String> {
    /**
     * Контроллер
     */
    Controller controller;

    public PublicUtilityValidatorImpl(Controller controller) {
        this.controller = controller;
    }

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
