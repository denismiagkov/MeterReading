package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;

import java.time.LocalDateTime;


public class MeterReadingReviewActualDto extends IncomingDtoParent {
    private int userId;
    private ActionType action;

    @Override
    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public MeterReadingReviewActualDto() {
        this.action = ActionType.REVIEW_ACTUAL_READING;
    }

}
