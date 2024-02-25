package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.outgoing.UserActionDto;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.starter.audit.service.AuditService;

import java.util.List;

/**
 * Интерфейс, объявляющий логику обработки данных о действиях, совершенных пользователями в приложении
 */
public interface UserActivityService extends AuditService<ActionType> {

    /**
     * Метод возвращает список всех действий, совершенных пользователями в приложении
     *
     * @param pageable Праматры пагинации
     * @return List<UserActionDto> Список всех действий пользователей в приложении
     */
    List<UserActionDto> getUserActivitiesList(Pageable pageable);
}
