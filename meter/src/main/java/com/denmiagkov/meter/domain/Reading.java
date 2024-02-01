package com.denmiagkov.meter.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс показаний счетчика
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString(of = {"date", "values"})
public class Reading {
    /**
     * Уникальный идентификатор показания
     */
    UUID id = UUID.randomUUID();
    /**
     * Идентификатор пользователя
     */
    UUID userId;
    /**
     * Дата и время подачи показания
     */
    @Getter
    LocalDateTime date;
    /**
     * Значения счетчиков по типам услуг
     */
    @Getter
    Map<String, Double> values;

    /**
     * Конструктор
     */
    public Reading(User user, Map<String, Double> values) {
        this.userId = user.getId();
        this.date = LocalDateTime.now();
        this.values = values;
    }
}
