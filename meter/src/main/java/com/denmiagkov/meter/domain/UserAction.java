package com.denmiagkov.meter.domain;

import com.denmiagkov.meter.domain.ActionType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс действия пользователя
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
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

    /**
     * Конструкторы, геттеры, сеттеры
     */
    public UserAction(int userId, ActionType action) {
        this.userId = userId;
        this.dateTime = LocalDateTime.now();
        this.action = action;
    }
}
