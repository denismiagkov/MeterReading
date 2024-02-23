package com.denmiagkov.starter.audit.dto;

/**
 * Класс-родитель для входящих ДТО
 */
public abstract class IncomingDto<E extends Enum<E>> {

    public abstract Integer getUserId();

    public abstract Enum<E> getAction();
}
