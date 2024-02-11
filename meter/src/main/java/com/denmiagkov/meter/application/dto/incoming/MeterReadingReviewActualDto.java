package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;


public class MeterReadingReviewActualDto extends IncomingDto {
    private int userId;
    private int utilityId;
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

    public int getUtilityId() {
        return utilityId;
    }

    public void setUtilityId(int utilityId) {
        this.utilityId = utilityId;
    }

    public MeterReadingReviewActualDto() {
        this.action = ActionType.REVIEW_ACTUAL_READING;
    }

}
