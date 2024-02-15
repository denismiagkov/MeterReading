package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;
/**
 * Исключение, выбрасываемое при попытке повторной подачи показаний в текущем месяце
 * */
public class SubmitReadingOnTheSameMonthException extends ValidationException {
    public SubmitReadingOnTheSameMonthException(){
        super("Показания могут подаваться не чаще одного раза в месяц!");
    }
}
