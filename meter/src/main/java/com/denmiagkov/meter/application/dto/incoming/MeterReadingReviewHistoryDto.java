package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;

/**
 * Класс входящего ДТО для просмотра пользователем истории подачи показаний
 */
public class MeterReadingReviewHistoryDto extends IncomingDto {
    /**
     * id пользователя
     */
    private int userId;
    /**
     * Тип действия пользователя
     */
    private ActionType action;

    /**
     * Конструктор, геттерф и сеттеры
     */
    public MeterReadingReviewHistoryDto() {
        this.action = ActionType.REVIEW_READINGS_HISTORY;
    }

    public Integer getUserId() {
        return userId;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
