package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;

/**
 * Входящее ДТО для обзора показаний счетчиков за определенный месяц
 */
public class MeterReadingReviewForMonthDto extends IncomingDto {
    /**
     * id пользователя
     */
    private int userId;
    /**
     * Тип действия пользователя
     */
    private ActionType action;
    /**
     * Год подачи показаний
     */
    private int year;
    /**
     * Месяц подачи показаний
     */
    private int month;

    /**
     * Конструктор, геттеры и сеттеры
     */
    public MeterReadingReviewForMonthDto() {
        this.action = ActionType.REVIEW_READINGS_FOR_MONTH;
    }

    @Override
    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
