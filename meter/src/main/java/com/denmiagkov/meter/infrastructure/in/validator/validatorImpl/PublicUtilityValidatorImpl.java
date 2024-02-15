package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.DictionaryServiceImpl;
import com.denmiagkov.meter.infrastructure.in.validator.exception.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.validator.DtoValidator;

/**
 * Класс, валидирующий данные о новом типе услуг (показаний счетчиков), добавляемых  в справочник
 */
public class PublicUtilityValidatorImpl implements DtoValidator<String> {

    public static final PublicUtilityValidatorImpl INSTANCE = new PublicUtilityValidatorImpl();

    private static final DictionaryService dictionaryService = DictionaryServiceImpl.INSTANCE;

    private PublicUtilityValidatorImpl() {
    }

    /**
     * Метод проверяет, что добавляемый тип услуг отсутствует в справочнике
     *
     * @param newUtility название нового типа услуг
     * @return booleqn true в случае успешной проверки, в противном случае - false
     */
    @Override
    public boolean isValid(String newUtility) {
        var utilitiesDictionary = dictionaryService.getUtilitiesDictionary();
        if (!utilitiesDictionary.containsValue(newUtility.toUpperCase())) {
            return true;
        } else {
            throw new PublicUtilityTypeAlreadyExistsException(newUtility);
        }
    }
}
