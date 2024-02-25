package com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.validatorImpl;

import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.DtoValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Класс, валидирующий данные о новом типе услуг (показаний счетчиков), добавляемых  в справочник
 */
@Component
@AllArgsConstructor
public class PublicUtilityValidatorImpl implements DtoValidator<String> {

    private final DictionaryService dictionaryService;

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
