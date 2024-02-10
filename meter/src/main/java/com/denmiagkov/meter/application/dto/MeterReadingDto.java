package com.denmiagkov.meter.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class MeterReadingDto {
    /**
     * Идентификатор пользователя
     */
    int userId;
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
}
