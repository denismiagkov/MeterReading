package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;
/**
 * Класс-родитель для входящих ДТО
 * */
public abstract class IncomingDto {

    public abstract Integer getUserId();

    public abstract ActionType getAction();
}
