package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.incoming.SubmitNewMeterReadingDto;
import com.denmiagkov.meter.domain.MeterReading;

import java.util.List;

/**
 * Интерфейс, объявляющий логику взаимодействия с базой данных по поводу сведений о показаниях счетчиков
 */
public interface MeterReadingRepository {

    /**
     * Метод добавления нового показания счетчика в базу данных
     *
     * @param reading новое показание счетчика
     * @return MeterReading Новое показание счетчика
     */
    MeterReading addNewMeterReading(MeterReading reading);

    /**
     * Метод выборки данных об актуальных показаниях счетчиков определенного пользователя
     *
     * @param userId id пользователя
     * @return List<MeterReading> Список актуальных показаний пользователя
     */
    List<MeterReading> findActualMeterReadingsOnAllUtilitiesByUser(int userId);

    /**
     * Метод просмотра актуального (последнего переданного) показания счетчика конкретного пользователя
     * по определенному типу услуг
     *
     * @param userId    id пользователя
     * @param utilityId Тип показаний (услуг)
     * @return MeterReading последние переданные показания счетчиков
     */
    MeterReading findActualMeterReadingOnExactUtility(int userId, int utilityId);

    /**
     * Метод выборки всех показаний счетчиков, переданных всеми пользоваетелями
     *
     * @param pageable Параметры пагинации
     * @return List<MeterReading> Список всех переданных показаний
     */
    List<MeterReading> findAllMeterReadings(Pageable pageable);

    /**
     * Метод получения истории передачи показаний конкретным пользователем
     *
     * @param userId id пользователя
     * @return List<MeterReading> Список показаний, переданных указанным пользователем
     */
    List<MeterReading> findMeterReadingsHistory(int userId, Pageable pageable);

    /**
     * Метод получения всех показаний, переданных указанным пользователем за определенный месяц
     *
     * @param userId id пользователя
     * @param year   Год
     * @param month  Месяц
     * @return List<MeterReading> Список показаний счетчиков пользователя за указанные год и месяц
     */
    List<MeterReading> findMeterReadingsForExactMonthByUser(int userId, int year, int month);
}
