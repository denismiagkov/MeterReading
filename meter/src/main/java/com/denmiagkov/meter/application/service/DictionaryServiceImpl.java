package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.exception.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.application.repository.DictionaryRepository;
import com.denmiagkov.meter.application.repository.DictionaryRepositoryImpl;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Класс реализует логику обработки данных о справочнике показаний (типов услуг)
 */
public class DictionaryServiceImpl implements DictionaryService {
public static final DictionaryServiceImpl INSTANCE= new DictionaryServiceImpl();

    /**
     * Репозиторий справочника показаний
     */
    DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl() {
        this.dictionaryRepository = DictionaryRepositoryImpl.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, String> addUtilityTypeToDictionary(String utilityName) {
        Map<Integer, String> utilitiesDictionary = dictionaryRepository.getAllUtilitiesTypes();
        if (!utilitiesDictionary.containsValue(utilityName)) {
            dictionaryRepository.addUtilityTypeToDictionary(utilityName.toUpperCase());
            return getUtilitiesDictionary();
        } else {
            throw new PublicUtilityTypeAlreadyExistsException(utilityName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, String> getUtilitiesDictionary() {
        return dictionaryRepository.getAllUtilitiesTypes();
    }
}
