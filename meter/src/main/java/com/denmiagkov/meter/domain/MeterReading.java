package com.denmiagkov.meter.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Класс показаний счетчика
 */
@Data
@ToString(of = {"date", "values"})
public class MeterReading {
    /**
     * Уникальный идентификатор показания
     */
    private final UUID id = UUID.randomUUID();
    /**
     * Идентификатор пользователя
     */
    private final UUID userId;
    /**
     * Дата и время подачи показания
     */
    @Getter
    private final LocalDateTime date;
    /**
     * Значения счетчиков по типам услуг
     */
    @Getter
    private final Map<String, Double> values;

    /**
     * Конструктор
     */
    public MeterReading(User user, Map<String, Double> values) {
        this.userId = user.getId();
        this.date = LocalDateTime.now();
        this.values = values;
    }
}
