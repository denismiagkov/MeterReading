package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.starter.audit.dto.IncomingDto;
import lombok.Getter;
import lombok.Setter;

/**
 * Входящее ДТО для обзора показаний счетчиков за определенный месяц
 */
@Getter
@Setter
public class ReviewMeterReadingForMonthDto extends IncomingDto<ActionType> {
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

    public ReviewMeterReadingForMonthDto() {
        this.action = ActionType.REVIEW_READINGS_FOR_MONTH;
    }

    @Override
    public ActionType getAction() {
        return action;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }
}
