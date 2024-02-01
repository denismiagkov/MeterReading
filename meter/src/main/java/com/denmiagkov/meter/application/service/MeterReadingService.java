package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;

import java.util.List;

public interface MeterReadingService {

    /**
     * Метод подачи показаний
     *
     * @param user    Пользователь
     * @param reading Показания счетчиков
     */
    void submitNewReading(User user, MeterReading reading);

    /**
     * Метод получения всех показаний всех пользователей
     *
     * @return List<Reading> Общий список показаний счетчиков
     */
    List<List<MeterReading>> getAllReadingsList(int pageSize);

    /**
     * Метод получения актуальных (последних переданных) показаний счетчика конкретным пользователем
     *
     * @param user Пользователь
     * @return Reading Актуальные показания счетчиков
     */
    MeterReading getActualReadingByUser(User user);

    /**
     * Метод просмотра истории подачи показаний
     *
     * @param user Пользователь
     * @return List<Reading> Список поданных показаний
     */
    List<List<MeterReading>> getReadingsHistoryByUser(User user, int pageSize);

    /**
     * Метод просмотра показаний за конкретный месяц
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     */
    MeterReading getReadingsForMonthByUser(User user, int year, int month);
}
