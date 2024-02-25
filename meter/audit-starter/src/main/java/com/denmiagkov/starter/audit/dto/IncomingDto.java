package com.denmiagkov.starter.audit.dto;

/**
 * Параметризированный класс-родитель для входящих ДТО. Объявляет абстрактный метод getAction(),
 * который может возвращать любые отслеживаемые виды активности пользователей, определяемые на уровне приложения
 */
public abstract class IncomingDto<E extends Enum<E>> {

    public abstract Integer getUserId();

    public abstract Enum<E> getAction();
}
