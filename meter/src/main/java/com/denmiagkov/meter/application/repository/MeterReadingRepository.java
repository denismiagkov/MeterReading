package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;

import java.util.List;

/**
 * Интерфейс, объявляющий логику взаимодействия с базой данных по поводу сведений о показаниях счетчиков
 */
public interface MeterReadingRepository {


    /**
     * Метод добавления нового показания счетчика в базу данных
     *
     * @param reading новое показание счетчика
     */
    void addNewMeterReading(MeterReading reading);

    /**
     * Метод выборки данных об актуальных показаниях счетчиков определенного пользователя
     *
     * @param user Пользователь
     * @return List<MeterReading> Список актуальных показаний пользователя
     */
    List<MeterReading> getActualMeterReadingsOnAllUtilitiesByUser(User user);

    /**
     * Метод просмотра актуального (последнего переданного) показания счетчика конкретного пользователя
     * по определенному типу услуг
     *
     * @param user      Пользователь
     * @param utilityId Тип показаний (услуг)
     * @return MeterReading последние переданные показания счетчиков
     */
    MeterReading getActualMeterReadingOnExactUtility(User user, int utilityId);

    /**
     * Метод выборки всех показаний счетчиков, переданных всеми пользоваетелями
     *
     * @return List<MeterReading> Список всех переданных показаний
     */
    List<MeterReading> getAllMeterReadings();

    /**
     * Метод получения истории передачи показаний конкретным пользователем
     *
     * @param user Пользователь
     * @return List<MeterReading> Список показаний, переданных указанным пользователем
     */
    List<MeterReading> getMeterReadingsHistory(User user);

    /**
     * Метод получения всех показаний, переданных указанным пользователем за определенный месяц
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     * @return List<MeterReading> Список показаний счетчиков пользователя за указанные год и месяц
     */
    List<MeterReading> getMeterReadingsForExactMonthByUser(User user, int year, int month);
}
