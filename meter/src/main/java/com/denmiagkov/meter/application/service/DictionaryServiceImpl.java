package com.denmiagkov.meter.application.service;

import static com.denmiagkov.meter.utils.PublicUtility.PUBLIC_UTILITY;

public class DictionaryServiceImpl implements DictionaryService {
    /**
     * {@inheritDoc}
     */
    @Override
    public void addUtilityType(String newUtility) {
        PUBLIC_UTILITY.addUtilityType(newUtility);
    }
}
