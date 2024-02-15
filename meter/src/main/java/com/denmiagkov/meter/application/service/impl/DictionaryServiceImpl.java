package com.denmiagkov.meter.application.service.impl;

import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.application.repository.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Класс реализует логику обработки данных о справочнике показаний (типов услуг)
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    /**
     * Репозиторий справочника показаний
     */
    private final DictionaryRepository dictionaryRepository;

    @Autowired
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
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
