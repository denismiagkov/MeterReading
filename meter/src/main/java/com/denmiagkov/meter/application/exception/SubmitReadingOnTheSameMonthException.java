package com.denmiagkov.meter.application.exception;
/**
 * Исключение, выбрасываемое при попытке повторной подачи показаний в текущем месяце
 * */
public class SubmitReadingOnTheSameMonthException extends RuntimeException {
    public SubmitReadingOnTheSameMonthException(){
        super("Показания могут подаваться не чаще одного раза в месяц!");
    }
}
