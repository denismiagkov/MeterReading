package com.denmiagkov.meter.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс действия пользователя
 */
@Data
@EqualsAndHashCode(of = "id")
public class Activity {
    /**
     * Уникальный идентификатор действия
     */
    private final UUID id = UUID.randomUUID();
    /**
     * Идентификатор пользователя, совершившего действие
     */
    private final UUID userId;
    /**
     * Дата и время совершения действия
     */
    private final LocalDateTime dateTime;
    /**
     * Тип действия
     */
    private final ActivityType action;

    /**
     * Конструктор
     */
    public Activity(User user, ActivityType action) {
        this.userId = user.getId();
        this.dateTime = LocalDateTime.now();
        this.action = action;
    }
}
