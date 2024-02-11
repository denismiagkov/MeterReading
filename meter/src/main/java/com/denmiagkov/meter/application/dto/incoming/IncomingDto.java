package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;

public abstract class IncomingDto {

    public abstract Integer getUserId();

    public abstract ActionType getAction();
}
