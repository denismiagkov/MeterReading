package com.denmiagkov.meter.application.dto.outgoing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Исходящее ДТО для маппинга объекта показания счетчика
 */
@NoArgsConstructor
@Getter
@Setter
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
