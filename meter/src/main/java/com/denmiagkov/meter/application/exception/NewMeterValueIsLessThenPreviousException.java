package com.denmiagkov.meter.application.exception;

/**
 * Исключение, выбрасываемое при подаче новых показаний, в случае если их значение меньше текущих показаний
 */
public class NewMeterValueIsLessThenPreviousException extends RuntimeException {
    public NewMeterValueIsLessThenPreviousException() {
        super("Недопустимое знаачение: новое показание счетчика меньше предыдущего!");
    }
}
