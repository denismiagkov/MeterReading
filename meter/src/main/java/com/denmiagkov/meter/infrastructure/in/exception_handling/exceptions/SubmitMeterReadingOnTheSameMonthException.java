package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;
/**
 * Исключение, выбрасываемое при попытке повторной подачи показаний в текущем месяце
 * */
public class SubmitMeterReadingOnTheSameMonthException extends ValidationException {
    public SubmitMeterReadingOnTheSameMonthException(){
        super("Показания могут подаваться не чаще одного раза в месяц!");
    }
}
