package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.Pageable;
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
     * @param meterReading Входящее ДТО нового показания счетчика
     * @return MeterReadingDto Исходящее ДТО добавленного показания счетчика
     */
    MeterReadingDto submitNewMeterReading(SubmitNewMeterReadingDto meterReading);

    /**
     * Метод получения всех показаний счетчиков всех пользователей с учетом параметров пагинации
     *
     * @param pageable Параметры пагинации
     * @return List<List < MeterReading>> Общий список показаний счетчиков с учетом параметров панинации
     */
    List<MeterReadingDto> getAllMeterReadingsList(Pageable pageable);

    /**
     * Метод получения актуального (последнего переданного) показания счетчика конкретного пользователя
     *
     * @param requestDto      Входящее ДТО на предоставление актуального показания счетчика
     * @return MeterReadingDto Исходящее ДТО актуального показания счетчика
     */
    MeterReadingDto getActualMeterReadingOnExactUtilityByUser(ReviewActualMeterReadingDto requestDto);

    /**
     * Метод получения всех актуальных (последних переданноых) показаний счетчиков конкретного пользователя
     *
     * @param requestDto Входящее ДТО на предоставление актуальных показаний счетчика
     * @return List<MeterReading> Список актуальных показаний счетчика
     */
    List<MeterReadingDto> getActualMeterReadingsOnAllUtilitiesByUser(ReviewActualMeterReadingDto requestDto);

    /**
     * Метод просмотра истории подачи показаний конкретным пользователем с учетом параметров пагинации
     *
     * @param requestDto    Входящее ДТО на предоствление истории подачи показаний пользователем
     * @param pageable Параметры пагинации
     * @return List < MeterReadingDto> Список поданных показаний с учетом параметров пагинации
     */
    List<MeterReadingDto> getMeterReadingsHistoryByUser(ReviewMeterReadingHistoryDto requestDto, Pageable pageable);

    /**
     * Метод просмотра показаний счетчиков определенного пользователя за конкретный месяц
     *
     * @param requestDto Входящее ДТО с информацией о пользователе, месяце и годе подачи показаний счетчика
     * @return List<MeterReadingDto> Список показаний счетчиков
     */
    List<MeterReadingDto> getReadingsForMonthByUser(ReviewMeterReadingForMonthDto requestDto);
}
