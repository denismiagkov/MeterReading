package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.domain.UserAction;

import java.util.List;

/**
 * Класс, отвечающий за взаимодействие с базой данных по поводу сведений о действиях пользователей в системе
 */
public interface ActivityRepository {

    /**
     * Метод добавляет в базу данных запись о действии, совершенном пользователем в приложении
     *
     * @param userAction Новое действие пользователя
     * @return boolean true - в случае успешного добавления,в противном случае - false
     */
    boolean addUserAction(UserAction userAction);

    /**
     * Метод получения всех записей о действиях, совершенных пользователями в системе
     *
     * @param pageable Параметры пагинации
     * @return List<Activity> Список действий пользователей
     */
    List<UserAction> findAllUsersActions(Pageable pageable);
}
