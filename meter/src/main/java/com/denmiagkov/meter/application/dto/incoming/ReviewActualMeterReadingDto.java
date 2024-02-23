package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.starter.audit.dto.IncomingDto;
import lombok.Getter;
import lombok.Setter;

/**
 * Входящее ДТО для просмотра текущих показаний счетчиков
 */
@Getter
@Setter
public class ReviewActualMeterReadingDto extends IncomingDto<ActionType> {
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

    public ReviewActualMeterReadingDto() {
        this.action = ActionType.REVIEW_ACTUAL_READING;
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
