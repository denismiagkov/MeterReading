package com.denmiagkov.meter.application.dto.outgoing;

import com.denmiagkov.meter.domain.ActionType;

import java.time.LocalDateTime;

/**
 * Исходящее ДТО для маппинга объекта показания счетчика
 */
public class MeterReadingDto {
    /**
     * Идентификатор пользователя
     */
    private int userId;
    /**
     * Дата и время подачи показания
     */
    private LocalDateTime date;
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
