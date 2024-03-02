package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.starter.audit.dto.IncomingDto;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс входящего ДТО для просмотра пользователем истории подачи показаний
 */
@Getter
@Setter
public class ReviewMeterReadingHistoryDto extends IncomingDto<ActionType> {
    /**
     * id пользователя
     */
    private int userId;
    /**
     * Тип действия пользователя
     */
    private ActionType action;

    public ReviewMeterReadingHistoryDto() {
        this.action = ActionType.REVIEW_READINGS_HISTORY;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    @Override
    public ActionType getAction() {
        return action;
    }
}
