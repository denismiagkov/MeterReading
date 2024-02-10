package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class UserActivityDto {
    /**
     * Идентификатор пользователя, совершившего действие
     */
    int userId;
    /**
     * Дата и время совершения действия
     */
    LocalDateTime dateTime;
    /**
     * Тип действия
     */
    ActionType action;
}
