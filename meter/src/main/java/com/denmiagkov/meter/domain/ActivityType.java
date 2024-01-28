package com.denmiagkov.meter.domain;

/**
 * Перечисление типов действий пользователей
 */
public enum ActivityType {
    /**
     * Авторизация
     */
    AUTHORIZATION,
    /**
     * Передача показаний
     */
    SUBMIT_NEW_READING,
    /**
     * Выход из приложения
     */
    EXIT,
    /**
     * Регистрация
     */
    REGISTRATION,
    /**
     * Просмотр актуальных показаний счетчиков
     */
    REVIEW_ACTUAL_READING,
    /**
     * Проосмотр истории передачи показаний
     */
    REVIEW_CONVEYING_READINGS_HISTORY,
    /**
     * Просмотр показаний счетчиков за выбранный месяц
     */
    REVIEW_READINGS_FOR_MONTH,

}
