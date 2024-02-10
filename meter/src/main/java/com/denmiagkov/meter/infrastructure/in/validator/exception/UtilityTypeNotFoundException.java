package com.denmiagkov.meter.infrastructure.in.validator.exception;

public class UtilityTypeNotFoundException extends RuntimeException {
    public UtilityTypeNotFoundException() {
        super("This type of utilities is not registered!");
    }
}
