package com.denmiagkov.meter.infrastructure.in.validator.exception;
/**
 * Исключение, выбрасываемое при попытке повторной подачи показаний в текущем месяце
 * */
public class SubmitReadingOnTheSameMonthException extends RuntimeException {
    public SubmitReadingOnTheSameMonthException(){
        super("Показания могут подаваться не чаще одного раза в месяц!");
    }
}
