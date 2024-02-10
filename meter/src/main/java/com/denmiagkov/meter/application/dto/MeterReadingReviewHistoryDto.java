package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;


public class MeterReadingReviewHistoryDto extends IncomingDtoParent{
     int userId;
     ActionType action;
    public Integer getUserId() {
        return userId;
    }
    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }



    public void setUserId(int userId) {
        this.userId = userId;
    }
    public MeterReadingReviewHistoryDto() {
        this.action = ActionType.REVIEW_READINGS_HISTORY;
    }

}
