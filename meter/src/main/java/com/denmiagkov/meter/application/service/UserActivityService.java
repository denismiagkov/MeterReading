package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.domain.Activity;

import java.util.List;

public interface UserActivityService {

    /**
     * Метод добавляет действие пользователя в коллекцию
     */
    public boolean addActivity(Activity activity);

    /**
     * Метод возвращает список всех действий, совершенных пользователями
     *
     * @return List<Activity>
     */
    public List<Activity> getUserActivitiesList();
}
