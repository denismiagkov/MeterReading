package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.domain.Activity;

import java.util.List;

/**
 * Интерфейс, объявляющий логику обработки данных о действиях, совершенных пользователями в приложении
 */
public interface UserActivityService {

    /**
     * Метод добавляет действие пользователя в базу данных
     *
     * @param activity действие пользователя в приложении
     * @return boolean true - в случае успешного добавления,в противном случае - false
     */
    boolean addActivity(Activity activity);

    /**
     * Метод возвращает список всех действий, совершенных пользователями в приложении
     *
     * @return List<Activity> Список всех действий пользователей в приложении
     */
    List<Activity> getUserActivitiesList();
}
