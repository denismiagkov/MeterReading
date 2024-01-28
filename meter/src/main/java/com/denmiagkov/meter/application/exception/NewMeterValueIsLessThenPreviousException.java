package com.denmiagkov.meter.application.exception;

/**
 * Исключение, выбрасываемое при подаче новыйх показаний. в случае если их значение меньше текущих показаний
 */
public class NewMeterValueIsLessThenPreviousException extends RuntimeException {
    public NewMeterValueIsLessThenPreviousException(String message) {
        super(message);
    }
}
