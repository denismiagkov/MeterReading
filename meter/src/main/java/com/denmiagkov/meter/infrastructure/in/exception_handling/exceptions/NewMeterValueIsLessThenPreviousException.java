package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

/**
 * Исключение, выбрасываемое при подаче новых показаний, в случае если их значение меньше текущих показаний
 */
public class NewMeterValueIsLessThenPreviousException extends ValidationException {
    public NewMeterValueIsLessThenPreviousException() {
        super("Недопустимое значение: новое показание счетчика меньше предыдущего!");
    }
}
