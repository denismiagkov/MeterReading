package com.denmiagkov.meter.application.dto.outgoing;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserRole;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Исходящее ДТО действия пользователя в приложении
 * */
public class UserActionDto{
    /**
     * Уникальный идентификатор действия
     */
     int id;
    /**
     * Идентификатор пользователя, совершившего действие
     */
     final int userId;
    /**
     * Дата и время совершения действия
     */
     final LocalDateTime dateTime;
    /**
     * Тип действия
     */
     final ActionType action;

    public UserActionDto(int id, int userId, LocalDateTime dateTime, ActionType action) {
        this.id = id;
        this.userId = userId;
        this.dateTime = dateTime;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
