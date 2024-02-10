package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;

import java.time.LocalDateTime;


public class MeterReadingSubmitDto extends IncomingDtoParent{
    private int userId;
    private ActionType action;

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

    public MeterReadingSubmitDto() {
        this.action = ActionType.SUBMIT_NEW_READING;
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
