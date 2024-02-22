package com.denmiagkov.starter.audit.service;


import com.denmiagkov.starter.audit.dto.IncomingDto;

/**
 * Интерфейс, объявляющий логику обработки данных о действиях, совершенных пользователями в приложении
 */
public interface AuditService {

    /**
     * Метод добавляет действие пользователя в базу данных
     *
     * @return boolean true - в случае успешного добавления,в противном случае - false
     */
    void registerUserAction(IncomingDto incomingDto);
}
