package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.*;
import com.denmiagkov.meter.domain.*;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.ListUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис подачи показаний
 */
@AllArgsConstructor
public class MeterReadingServiceImpl implements MeterReadingService {
    /**
     * Репозиторий данных о показаниях счетчика
     */
    private final MeterReadingRepository meterReadingRepository;
    /**
     * Репозиторий данных о действиях пользователя
     */
    private final UserActivityService activityService;


    /**
     * {@inheritDoc}
     */
    @Override
    public void submitNewReading(User user, MeterReading reading) {
        meterReadingRepository.addNewMeterReading(reading);
        Activity activity = new Activity(user, ActivityType.SUBMIT_NEW_READING);
        activityService.addActivity(activity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReading>> getAllReadingsList(int pageSize) {
        List<MeterReading> meterReadingList = meterReadingRepository.getAllMeterReadings();
        List<List<MeterReading>> meterReadingPages = ListUtils.partition(meterReadingList, pageSize);
        return meterReadingPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReading getActualMeterReadingOnExactUtilityByUser(User user, int utilityId) {
        return meterReadingRepository.getActualMeterReadingOnExactUtility(user, utilityId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReading> getActualMeterReadingsOnAllUtilitiesByUser(User user) {
        Activity activity = new Activity(user, ActivityType.REVIEW_ACTUAL_READING);
        activityService.addActivity(activity);
        return meterReadingRepository.getActualMeterReadingsOnAllUtilitiesByUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReading>> getMeterReadingsHistoryByUser(User user, int pageSize) {
        Activity activity = new Activity(user, ActivityType.REVIEW_CONVEYING_READINGS_HISTORY);
        activityService.addActivity(activity);
        List<MeterReading> meterReadingHistoryByList = meterReadingRepository.getMeterReadingsHistory(user);
        List<List<MeterReading>> meterReadingHistoryByPages =
                ListUtils.partition(meterReadingHistoryByList, pageSize);
        return meterReadingHistoryByPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReading> getReadingsForMonthByUser(User user, int year, int month) {
        Activity activity = new Activity(user, ActivityType.REVIEW_READINGS_FOR_MONTH);
        activityService.addActivity(activity);
        return meterReadingRepository.getMeterReadingsForExactMonthByUser(user, year, month);
    }
}
