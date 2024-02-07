package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.UserActivity;

import java.util.List;

/**
 * Класс, отвечающий за взаимодействие с базой данных по поводу сведений о действиях пользователей в системе
 */
public interface ActivityRepository {

    /**
     * Метод добавляет в базу данных запись о действии, совершенном пользователем в приложении
     *
     * @param userActivity Новое действие пользователя
     * @return boolean true - в случае успешного добавления,в противном случае - false
     */
    boolean addActivity(UserActivity userActivity);

    /**
     * Метод получения всех записей о действиях, совершенных пользователями в системе
     *
     * @return List<Activity> Список действий пользователей
     */
    List<UserActivity> getActivitiesList();
}
