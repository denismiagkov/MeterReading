package com.denmiagkov.meter.application.service.impl;

import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.repository.DictionaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Класс реализует логику обработки данных о справочнике показаний (типов услуг)
 */
@Service
@AllArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, String> addUtilityType(String utilityName) {
        return dictionaryRepository.addUtilityType(utilityName.toUpperCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, String> getUtilitiesDictionary() {
        return dictionaryRepository.getAllUtilitiesTypes();
    }
}