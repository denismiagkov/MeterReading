package com.denmiagkov.starter.audit.dto;

import com.denmiagkov.starter.audit.domain.ActionType;

/**
 * Класс-родитель для входящих ДТО
 */
public abstract class IncomingDto {

    public abstract Integer getUserId();

    public abstract ActionType getAction();
}
