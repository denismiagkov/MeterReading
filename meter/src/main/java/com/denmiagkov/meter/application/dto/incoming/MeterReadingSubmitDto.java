package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;

import java.time.LocalDateTime;

/**
 * Класс входящего ДТО для передачи нового показания счетчика
 */
public class MeterReadingSubmitDto extends IncomingDto {
    /**
     * id пользователя
     */
    private int userId;
    /**
     * Тип действия пользователя
     */
    private ActionType action;
    /**
     * Дата и время подачи показания
     */
    private LocalDateTime date;
    /**
     * id типа услуг
     */
    private int utilityId;
    /**
     * Значение счетчика
     */
    private double value;

    /**
     * Конструктор, геттеры и сеттеры
     */
    public MeterReadingSubmitDto() {
        this.action = ActionType.SUBMIT_NEW_READING;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public Integer getUserId() {
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
