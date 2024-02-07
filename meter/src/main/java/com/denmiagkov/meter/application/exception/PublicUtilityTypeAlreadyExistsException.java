package com.denmiagkov.meter.application.exception;

/**
 * Исключение, выбрасываемое при попытке добавления нового типа коммунальных услуг, если он  уже присутствует в справочнике
 */
public class PublicUtilityTypeAlreadyExistsException extends RuntimeException {
    public PublicUtilityTypeAlreadyExistsException(String utilityName) {
        super("Public utility " + utilityName + " already exists!");
    }
}
