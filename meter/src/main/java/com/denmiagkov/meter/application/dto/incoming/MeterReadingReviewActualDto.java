package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
/**
 * Входящее ДТО для просмотра текущих показаний счетчиков
 */
public class MeterReadingReviewActualDto extends IncomingDto {
    /**
     * id пользователя
     */
    private int userId;
    /**
     * id типа услуг (показаний счетчика)
     */
    private int utilityId;
    /**
     * тип действия пользователя
     */
    private ActionType action;
    /**
     * Конструктор, геттеры и сеттеры
     */
    public MeterReadingReviewActualDto() {
        this.action = ActionType.REVIEW_ACTUAL_READING;
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

    public int getUtilityId() {
        return utilityId;
    }

    public void setUtilityId(int utilityId) {
        this.utilityId = utilityId;
    }
}
