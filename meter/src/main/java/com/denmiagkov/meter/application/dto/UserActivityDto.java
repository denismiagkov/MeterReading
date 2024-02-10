package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;
import lombok.Value;

import java.time.LocalDateTime;


public class UserActivityDto {
    /**
     * Идентификатор пользователя, совершившего действие
     */
    int userId;
    /**
     * Дата и время совершения действия
     */
    LocalDateTime dateTime;
    /**
     * Тип действия
     */
    ActionType action;

    public UserActivityDto() {
    }

    public UserActivityDto(int userId, LocalDateTime dateTime, ActionType action) {
        this.userId = userId;
        this.dateTime = dateTime;
        this.action = action;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }
}
