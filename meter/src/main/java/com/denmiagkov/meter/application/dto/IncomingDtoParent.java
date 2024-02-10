package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.ActionType;

public abstract class IncomingDtoParent {

    public abstract Integer getUserId();

    public abstract ActionType getAction();
}
