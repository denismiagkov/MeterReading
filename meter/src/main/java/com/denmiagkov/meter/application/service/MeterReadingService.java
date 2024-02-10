package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.MeterReadingSubmitDto;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс, объявляющий логику обработки данных о показаниях счетчиков
 */
public interface MeterReadingService {

    /**
     * Метод передачи нового показания
     *
     * @param user    Пользователь
     * @param reading Показание счетчика
     */
    void submitNewMeterReading(MeterReadingSubmitDto meterReading);

    /**
     * Метод получения всех показаний счетчиков всех пользователей с учетом параметров пагинации
     *
     * @param pageSize Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметров панинации
     */
    List<List<MeterReadingSubmitDto>> getAllReadingsList(int pageSize);

    /**
     * Метод получения актуального (последнего переданного) показания счетчика конкретного пользователя
     *
     * @param user      Пользователь
     * @param utilityId Тип услуги
     * @return MeterReading Актуальное показание счетчика
     */
    MeterReadingSubmitDto getActualMeterReadingOnExactUtilityByUser(MeterReadingSubmitDto meterReadingSubmitDto);

    /**
     * Метод получения всех актуальных (последних переданноых) показаний счетчиков конкретного пользователя
     *
     * @param user Пользователь
     * @return List<MeterReading> Список актуальных показаний счетчика
     */
    List<MeterReadingSubmitDto> getActualMeterReadingsOnAllUtilitiesByUser(int userId);

    /**
     * Метод просмотра истории подачи показаний конкретным пользователем с учетом параметров пагинации
     *
     * @param user     Пользователь
     * @param pageSize Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Список поданных показаний с учетом параметров пагинации
     */
    List<List<MeterReadingSubmitDto>> getMeterReadingsHistoryByUser(int userId, int pageSize);

    /**
     * Метод просмотра показаний счетчиков определенного пользователя за конкретный месяц
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     * @return List<MeterReading> Список показаний счетчиков
     */
    List<MeterReadingSubmitDto> getReadingsForMonthByUser(int userID, Map<String, Integer> month);
}
