package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.exception.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.application.repository.DictionaryRepository;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Класс реализует логику обработки данных о справочнике показаний (типов услуг)
 * */
public class DictionaryServiceImpl implements DictionaryService {
    /**
     * Справочник услуг
     * */
    public static Map<Integer, String> PUBLIC_UTILITIES_LIST;

    /**
     * Репозиторий справочника показаний
     * */
    DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
        PUBLIC_UTILITIES_LIST = getUtilitiesDictionary();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addUtilityTypeToDictionary(String utilityName) {
        Map<Integer, String> utilitiesDictionary = dictionaryRepository.getAllUtilitiesTypes();
        if (!utilitiesDictionary.containsValue(utilityName)) {
            int newUtilityId = dictionaryRepository.addUtilityTypeToDictionary(utilityName.toUpperCase());
            PUBLIC_UTILITIES_LIST.put(newUtilityId, utilityName.toUpperCase());
            return true;
        } else {
            throw new PublicUtilityTypeAlreadyExistsException(utilityName);
        }
    }
/**
 * {@inheritDoc}
 * */
    @Override
    public Map<Integer, String> getUtilitiesDictionary() {
        return dictionaryRepository.getAllUtilitiesTypes();
    }


}
