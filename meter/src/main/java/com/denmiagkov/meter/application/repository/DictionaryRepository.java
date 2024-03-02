package com.denmiagkov.meter.application.repository;

import java.util.Map;

/**
 * Интерфейс, объявляющий методы взаимодействия с базой данных по поводу справочника услуг
 */
public interface DictionaryRepository {
    /**
     * Метод добавляет в справочник новый тип коммунальных услуг (новый тип показаний)
     *
     * @param name Название типа услуг
     * @return Map<Integer, String> Уникальный идентификатор и название нового типа услуг
     */
    Map<Integer, String> addUtilityType(String name);

    /**
     * Метод возвращает коллекцию всех типов услуг (типов показаний)
     *
     * @return Map<Integer, String> Коллекция всех актуальных типов услуг (показаний)
     */
    Map<Integer, String> getAllUtilitiesTypes();
}
