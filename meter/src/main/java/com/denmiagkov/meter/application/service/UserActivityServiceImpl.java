package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.domain.UserActivity;
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
    public boolean addActivity(UserActivity userActivity) {
        return activityRepository.addActivity(userActivity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserActivity> getUserActivitiesList() {
        return activityRepository.getActivitiesList();
    }
}
