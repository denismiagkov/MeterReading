package com.denmiagkov.meter.application.exception;
/**
 * Исключение, выбрасываемое при попытке повторной подачи показаний в текущем месяце
 * */
public class SubmitReadingOnTheSameMonthException extends RuntimeException {
    public SubmitReadingOnTheSameMonthException(String message){
        super(message);
    }
}
