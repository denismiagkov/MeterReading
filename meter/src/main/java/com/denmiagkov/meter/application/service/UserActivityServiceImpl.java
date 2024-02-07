package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.domain.Activity;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Сервис, реализующий логику обработки данных о действиях пользователей в системе
 */
@AllArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {
    /**
     * Репозиторий данных о действиях пользователя
     */
    private final ActivityRepository activityRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addActivity(Activity activity) {
        return activityRepository.addActivity(activity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Activity> getUserActivitiesList() {
        return activityRepository.getActivitiesList();
    }
}
