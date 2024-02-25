package com.denmiagkov.starter.audit.service;


import com.denmiagkov.starter.audit.dto.IncomingDto;

/**
 * Интерфейс, объявляющий логику обработки данных о действиях, совершенных пользователями в приложении
 */
public interface AuditService<E extends Enum<E>> {

    /**
     * Метод добавляет действие пользователя в базу данных
     *
     * @param incomingDto Входящее ДТО
     */
    void registerUserAction(IncomingDto<E> incomingDto);
}
