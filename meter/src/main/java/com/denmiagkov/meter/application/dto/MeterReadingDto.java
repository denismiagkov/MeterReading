package com.denmiagkov.meter.application.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
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
    int utilityId;
    /**
     * Значение счетчика
     */
    double value;
}
