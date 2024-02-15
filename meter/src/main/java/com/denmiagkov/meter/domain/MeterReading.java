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
    public MeterReading(User user, int utilityId, double value) {
        this.userId = user.getId();
        this.date = LocalDateTime.now();
        this.utilityId = utilityId;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterReading that = (MeterReading) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getUtilityId() {
        return utilityId;
    }

    public double getValue() {
        return value;
    }

    public MeterReading(int id, int userId, LocalDateTime date, int utilityId, double value) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.utilityId = utilityId;
        this.value = value;
    }
}
