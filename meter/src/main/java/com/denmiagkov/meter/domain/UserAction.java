package com.denmiagkov.meter.domain;

import com.denmiagkov.meter.application.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Класс действия пользователя
 */
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class UserAction {
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

    @Builder
    public UserAction(int userId, ActionType action) {
        this.userId = userId;
        this.dateTime = LocalDateTime.now();
        this.action = action;
    }
}
