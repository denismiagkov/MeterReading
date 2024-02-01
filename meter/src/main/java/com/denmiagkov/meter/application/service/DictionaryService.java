package com.denmiagkov.meter.application.service;

/**
 * Интерфейс сервиса типов показаний счетчиков (услуг)
 */
public interface DictionaryService {
    /**
     * Метод добавляет новый тип услуг (расширяет перечень подаваемых показаний)
     *
     * @param newUtility новый тип подаваемых показаний
     */
    void addUtilityType(String newUtility);
}
