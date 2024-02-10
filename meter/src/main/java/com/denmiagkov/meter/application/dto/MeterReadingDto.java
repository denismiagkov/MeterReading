package com.denmiagkov.meter.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;


public class MeterReadingDto {
    /**
     * Идентификатор пользователя
     */
    int userId;
    /**
     * Дата и время подачи показания
     */
    LocalDateTime date;
    /**
     * Идентификатор типа услуг
     */
    private int utilityId;
    /**
     * Значение счетчика
     */
    private double value;

    public MeterReadingDto() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getUtilityId() {
        return utilityId;
    }

    public void setUtilityId(int utilityId) {
        this.utilityId = utilityId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
