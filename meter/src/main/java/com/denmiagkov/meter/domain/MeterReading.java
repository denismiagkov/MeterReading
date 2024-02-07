package com.denmiagkov.meter.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Класс показаний счетчика
 */

@ToString(of = {"date", "utilityId", "value"})
@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class MeterReading {
    /**
     * Уникальный идентификатор показания
     */
    private int id;
    /**
     * Идентификатор пользователя
     */
    private final int userId;
    /**
     * Дата и время подачи показания
     */
    private final LocalDateTime date;
    /**
     * Идентификатор типа услуг
     */
    private final int utilityId;
    /**
     * Значение счетчика
     */
    private final double value;

    /**
     * Конструктор
     */
    @Builder
    public MeterReading(User user, int utilityId, double value) {
        this.userId = user.getId();
        this.date = LocalDateTime.now();
        this.utilityId = utilityId;
        this.value = value;
    }
}
