package com.denmiagkov.meter.application.dto.outgoing;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Исходящее ДТО для маппинга объекта показания счетчика
 */
@NoArgsConstructor
@Data
public class MeterReadingDto {

    /**
     * Идентификатор пользователя
     */
    private int userId;

    /**
     * Дата и время подачи показания
     */
    private LocalDateTime date;

    /**
     * Идентификатор типа услуг
     */
    private int utilityId;

    /**
     * Значение счетчика
     */
    private double value;
}
