package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.starter.audit.dto.IncomingDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс входящего ДТО для передачи нового показания счетчика
 */
@Getter
@Setter
public class SubmitNewMeterReadingDto extends IncomingDto<ActionType> {
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

    public SubmitNewMeterReadingDto() {
        this.action = ActionType.SUBMIT_NEW_READING;
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
