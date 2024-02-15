package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

public class UtilityTypeNotFoundException extends ValidationException {
    public UtilityTypeNotFoundException() {
        super("This type of utilities is not registered!");
    }
}
