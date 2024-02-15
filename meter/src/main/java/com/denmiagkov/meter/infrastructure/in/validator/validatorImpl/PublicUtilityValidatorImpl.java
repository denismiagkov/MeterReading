package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.infrastructure.in.validator.DtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Класс, валидирующий данные о новом типе услуг (показаний счетчиков), добавляемых  в справочник
 */
@Component
public class PublicUtilityValidatorImpl implements DtoValidator<String> {
    private final DictionaryService dictionaryService;

    @Autowired
    public PublicUtilityValidatorImpl(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
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
