package com.denmiagkov.meter.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс действия пользователя
 */
public class UserAction {
    /**
     * Уникальный идентификатор действия
     */
    private int id;
    /**
     * Идентификатор пользователя, совершившего действие
     */
    private final int userId;
    /**
     * Дата и время совершения действия
     */
    private final LocalDateTime dateTime;
    /**
     * Тип действия
     */
    private final ActionType action;

    /**
     * Конструкторы, геттеры, сеттеры
     */
    public UserAction(int userId, ActionType action) {
        this.userId = userId;
        this.dateTime = LocalDateTime.now();
        this.action = action;
    }

    public UserAction(int id, int userId, LocalDateTime dateTime, ActionType action) {
        this.id = id;
        this.userId = userId;
        this.dateTime = dateTime;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public ActionType getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAction that = (UserAction) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
