package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.outgoing.UserActionDto;
import com.denmiagkov.meter.application.dto.incoming.IncomingDto;

import java.util.List;

/**
 * Интерфейс, объявляющий логику обработки данных о действиях, совершенных пользователями в приложении
 */
public interface UserActivityService {

    /**
     * Метод добавляет действие пользователя в базу данных
     *
     * @return boolean true - в случае успешного добавления,в противном случае - false
     */
    void registerUserAction(IncomingDto incomingDto);

    /**
     * Метод возвращает список всех действий, совершенных пользователями в приложении
     *
     * @return List<Activity> Список всех действий пользователей в приложении
     */
    List<List<UserActionDto>> getUserActivitiesList(int pageSize);
}
