package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewForMonthDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingSubmitDto;
import com.denmiagkov.meter.domain.MeterReading;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс, объявляющий логику взаимодействия с базой данных по поводу сведений о показаниях счетчиков
 */
public interface MeterReadingRepository {


    /**
     * Метод добавления нового показания счетчика в базу данных
     *
     * @param reading новое показание счетчика
     */
    MeterReading addNewMeterReading(MeterReadingSubmitDto reading);

    /**
     * Метод выборки данных об актуальных показаниях счетчиков определенного пользователя
     *
     * @param user Пользователь
     * @return List<MeterReading> Список актуальных показаний пользователя
     */
    List<MeterReading> getActualMeterReadingsOnAllUtilitiesByUser(int userId);

    /**
     * Метод просмотра актуального (последнего переданного) показания счетчика конкретного пользователя
     * по определенному типу услуг
     *
     * @param user      Пользователь
     * @param utilityId Тип показаний (услуг)
     * @return MeterReading последние переданные показания счетчиков
     */
    MeterReading getActualMeterReadingOnExactUtility(int userId, int utilityId);

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
    List<MeterReading> getMeterReadingsHistory(int userId);

    /**
     * Метод получения всех показаний, переданных указанным пользователем за определенный месяц
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     * @return List<MeterReading> Список показаний счетчиков пользователя за указанные год и месяц
     */
    List<MeterReading> getMeterReadingsForExactMonthByUser(int userId, int year, int month);
}
