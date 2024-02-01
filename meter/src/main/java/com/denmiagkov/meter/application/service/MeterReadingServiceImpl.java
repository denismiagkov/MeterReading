package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.application.repository.MeterReadingRepository;
import com.denmiagkov.meter.application.repository.UserRepository;
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
        meterReadingRepository.addNewReading(reading);
        Activity activity = new Activity(user, ActivityType.SUBMIT_NEW_READING);
        activityService.addActivity(activity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReading>> getAllReadingsList(int pageSize) {
        List<MeterReading> meterReadingList = meterReadingRepository.getAllReadingsList();
        List<List<MeterReading>> meterReadingPages = ListUtils.partition(meterReadingList, pageSize);
        return meterReadingPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReading getActualReadingByUser(User user) {
        Activity activity = new Activity(user, ActivityType.REVIEW_ACTUAL_READING);
        activityService.addActivity(activity);
        return meterReadingRepository.getLastReading(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReading>> getReadingsHistoryByUser(User user, int pageSize) {
        Activity activity = new Activity(user, ActivityType.REVIEW_CONVEYING_READINGS_HISTORY);
        activityService.addActivity(activity);
        List<MeterReading> meterReadingHistoryByList = meterReadingRepository.getReadingsHistory(user);
        List<List<MeterReading>> meterReadingHistoryByPages =
                ListUtils.partition(meterReadingHistoryByList, pageSize);
        return meterReadingHistoryByPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReading getReadingsForMonthByUser(User user, int year, int month) {
        Activity activity = new Activity(user, ActivityType.REVIEW_READINGS_FOR_MONTH);
        activityService.addActivity(activity);
        return meterReadingRepository.getReadingsForMonthByUser(user, year, month);
    }


    public static void main(String[] args) {
        MeterReadingRepository meterReadingRepository1 = new MeterReadingRepository();
        ActivityRepository activityRepository = new ActivityRepository();
        UserActivityServiceImpl userActivityService = new UserActivityServiceImpl(activityRepository);
        MeterReadingService meterReadingService = new MeterReadingServiceImpl(meterReadingRepository1, userActivityService);
        UserRepository userRepository = new UserRepository();
        UserServiceImpl userService = new UserServiceImpl(userRepository, userActivityService);
        User user1 = new User("John", "11-22-33", "Moscow", "user", "123");
        User user2 = new User("Paul", "11-22-33", "Moscow", "user", "123");
        User user3 = new User("George", "11-22-33", "Moscow", "user", "123");
        User user4 = new User("Ringo", "11-22-33", "Moscow", "user", "123");
        Map<String, Double> map1 = new HashMap<>();
        Map<String, Double> map2 = new HashMap<>();
        Map<String, Double> map3 = new HashMap<>();
        Map<String, Double> map4 = new HashMap<>();
        map1.put("HOT_WATER", 325.00);
        map1.put("COLD_WATER", 325.00);
        map1.put("HEATING", 325.00);
        map2.put("HOT_WATER", 425.00);
        map2.put("COLD_WATER", 425.00);
        map2.put("HEATING", 425.00);
        map3.put("HOT_WATER", 525.00);
        map3.put("COLD_WATER", 525.00);
        map3.put("HEATING", 525.00);
        map4.put("HOT_WATER", 625.00);
        map4.put("COLD_WATER", 625.00);
        map4.put("HEATING", 625.00);
        MeterReading meterReading1 = new MeterReading(user1, map1);
        MeterReading meterReading2 = new MeterReading(user2, map2);
        MeterReading meterReading3 = new MeterReading(user3, map3);
        MeterReading meterReading4 = new MeterReading(user4, map4);
        meterReadingService.submitNewReading(user1, meterReading1);
        meterReadingService.submitNewReading(user2, meterReading2);
        meterReadingService.submitNewReading(user3, meterReading3);
        meterReadingService.submitNewReading(user4, meterReading4);

        List<List<MeterReading>> result = meterReadingService.getAllReadingsList(1);
        int page = 0;
        for (List<MeterReading> currentPage : result) {
            System.out.println(currentPage);
            page++;
        }


    }
}
