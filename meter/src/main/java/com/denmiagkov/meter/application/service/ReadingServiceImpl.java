package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.domain.*;
import com.denmiagkov.meter.application.repository.Storage;
import lombok.AllArgsConstructor;

import java.util.List;

import static com.denmiagkov.meter.utils.PublicUtility.PUBLIC_UTILITY;

/**
 * Сервис подачи показаний
 */
@AllArgsConstructor
public class ReadingServiceImpl implements ReadingService {
    private final Storage storage;

    /**
     * Метод добавляет новый тип услуг (расширяет перечень подаваемых показаний)
     *
     * @param newUtility новый тип подаваемых показаний
     */
    @Override
    public void addUtilityType(String newUtility) {
        PUBLIC_UTILITY.addUtilityType(newUtility);
    }

    /**
     * Метод подачи показаний
     *
     * @param user    Пользователь
     * @param reading Показания счетчиков
     */
    @Override
    public void submitNewReading(User user, Reading reading) {
        storage.addNewReading(reading);
        Activity activity = new Activity(user, ActivityType.SUBMIT_NEW_READING);
        storage.addActivity(activity);
    }

    /**
     * Метод получения всех показаний всех пользователей
     *
     * @return List<Reading> Общий список показаний счетчиков
     */
    @Override
    public List<Reading> getAllReadingsList() {
        return storage.getAllReadingsList();
    }

    /**
     * Метод получения актуальных (последних переданных) показаний счетчика конкретным пользователем
     *
     * @param user Пользователь
     * @return Reading Актуальные показания счетчиков
     */
    @Override
    public Reading getActualReadingByUser(User user) {
        Activity activity = new Activity(user, ActivityType.REVIEW_ACTUAL_READING);
        storage.addActivity(activity);
        return storage.getLastReading(user);
    }

    /**
     * Метод просмотра истории подачи показаний
     *
     * @param user Пользователь
     * @return List<Reading> Список поданных показаний
     */
    @Override
    public List<Reading> getReadingsHistoryByUser(User user) {
        Activity activity = new Activity(user, ActivityType.REVIEW_CONVEYING_READINGS_HISTORY);
        storage.addActivity(activity);
        return storage.getReadingsHistory(user);
    }

    /**
     * Метод просмотра показаний за конкретный месяц
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     */
    @Override
    public Reading getReadingsForMonthByUser(User user, int year, int month) {
        Activity activity = new Activity(user, ActivityType.REVIEW_READINGS_FOR_MONTH);
        storage.addActivity(activity);
        return storage.getReadingsForMonthByUser(user, year, month);
    }
}
