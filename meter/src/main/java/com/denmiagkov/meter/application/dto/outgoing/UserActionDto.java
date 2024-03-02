package com.denmiagkov.meter.application.dto.outgoing;

import com.denmiagkov.meter.domain.ActionType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Исходящее ДТО действия пользователя в приложении
 */
@AllArgsConstructor
@Data
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
