package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;


public class MeterReadingReviewForMonthDto extends IncomingDtoParent {
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


    int year;
    int month;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }




    public MeterReadingReviewForMonthDto() {
        this.action = ActionType.REVIEW_READINGS_FOR_MONTH;
    }
}
