package com.denmiagkov.meter.domain;

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
public class UserActivity {
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
    public UserActivity(User user, ActivityType action) {
        this.userId = user.getId();
        this.dateTime = LocalDateTime.now();
        this.action = action;
    }
}
