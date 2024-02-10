package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserRole;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class UserActionDto implements Serializable {
    /**
     * Уникальный идентификатор действия
     */
    int id;
    /**
     * Идентификатор пользователя, совершившего действие
     */
    final int userId;
    /**
     * Дата и время совершения действия
     */
    final LocalDateTime dateTime;
    /**
     * Тип действия
     */
    final ActionType action;
}
