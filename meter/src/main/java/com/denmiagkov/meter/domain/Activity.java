package com.denmiagkov.meter.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс действия пользователя
 */
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Activity {
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
    private final ActivityType action;

    @Builder
    public Activity(User user, ActivityType action) {
        this.userId = user.getId();
        this.dateTime = LocalDateTime.now();
        this.action = action;
    }
}
