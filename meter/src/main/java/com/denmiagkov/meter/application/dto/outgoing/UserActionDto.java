package com.denmiagkov.meter.application.dto.outgoing;

import com.denmiagkov.starter.audit.domain.ActionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Исходящее ДТО действия пользователя в приложении
 */
@AllArgsConstructor
@Getter
@Setter
public class UserActionDto {

    /**
     * Уникальный идентификатор действия
     */
    private int id;
    /**
     * Идентификатор пользователя, совершившего действие
     */
    private final int userId;
    /**
     * Дата и время совершения действия
     */
    private final LocalDateTime dateTime;
    /**
     * Тип действия
     */
    private final ActionType action;
}
