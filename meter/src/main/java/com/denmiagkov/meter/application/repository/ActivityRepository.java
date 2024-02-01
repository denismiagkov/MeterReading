package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.Activity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за хранение данных о действиях пользователей в памяти приложения
 */
@Getter
@NoArgsConstructor
public class ActivityRepository {
    /**
     * Список всех действий, совершенных пользователями в системе
     */
    private static final List<Activity> ACTIVITIES = new ArrayList<>();

    /**
     * Метод добавления нового пользовательского действия в колекцию
     *
     * @param activity Новое действие пользователя
     * @return boolean true - в случае успешного добавления,в противном случае - false
     */
    public boolean addActivity(Activity activity) {
        return ACTIVITIES.add(activity);
    }

    /**
     * Метод получения всех действий, совершенных пользователями в системе
     *
     * @return List<Activity> Список действий пользователей
     */
    public List<Activity> getActivitiesList() {
        return ACTIVITIES;
    }


}
