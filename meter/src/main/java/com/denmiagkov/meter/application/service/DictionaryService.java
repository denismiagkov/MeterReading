package com.denmiagkov.meter.application.service;

import java.util.Map;

/**
 * Интерфейс сервиса типов показаний счетчиков (услуг)
 */
public interface DictionaryService {
    /**
     * Метод добавляет новый тип услуг (расширяет перечень подаваемых показаний)
     *
     * @param utilityName новый тип подаваемых показаний
     * @return Map<Integer, String> id и название добавленного типа показания
     */
    Map<Integer, String> addUtilityType(String utilityName);

    /**
     * Метод возвращает справочник типов услуг (типов показаний счетчиков)
     *
     * @return Map<Integer, String> Справочник показаний
     * */
    Map<Integer, String> getUtilitiesDictionary();

}
