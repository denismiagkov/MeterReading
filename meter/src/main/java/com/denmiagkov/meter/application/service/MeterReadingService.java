package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;

import java.util.List;

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
    MeterReadingDto submitNewMeterReading(MeterReadingSubmitDto meterReading);

    /**
     * Метод получения всех показаний счетчиков всех пользователей с учетом параметров пагинации
     *
     * @param pageSize Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметров панинации
     */
    List<MeterReadingDto> getAllMeterReadingsList(int page, int pageSize);

    /**
     * Метод получения актуального (последнего переданного) показания счетчика конкретного пользователя
     *
     * @param user      Пользователь
     * @param utilityId Тип услуги
     * @return MeterReading Актуальное показание счетчика
     */
    MeterReadingDto getActualMeterReadingOnExactUtilityByUser(MeterReadingReviewActualDto requestDto);

    /**
     * Метод получения всех актуальных (последних переданноых) показаний счетчиков конкретного пользователя
     *
     * @param user Пользователь
     * @return List<MeterReading> Список актуальных показаний счетчика
     */
    List<MeterReadingDto> getActualMeterReadingsOnAllUtilitiesByUser(MeterReadingReviewActualDto requestDto);

    /**
     * Метод просмотра истории подачи показаний конкретным пользователем с учетом параметров пагинации
     *
     * @param user     Пользователь
     * @param pageSize Параметр пагинации (размер страницы)
     * @return List<List < MeterReading>> Список поданных показаний с учетом параметров пагинации
     */
    List<MeterReadingDto> getMeterReadingsHistoryByUser(MeterReadingReviewHistoryDto requestDto);

    /**
     * Метод просмотра показаний счетчиков определенного пользователя за конкретный месяц
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     * @return List<MeterReading> Список показаний счетчиков
     */
    List<MeterReadingDto> getReadingsForMonthByUser(MeterReadingReviewForMonthDto requestDto);
}
