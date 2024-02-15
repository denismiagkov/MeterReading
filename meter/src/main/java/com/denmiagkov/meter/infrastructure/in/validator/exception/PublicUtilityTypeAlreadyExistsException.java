package com.denmiagkov.meter.infrastructure.in.validator.exception;

/**
 * Исключение, выбрасываемое при попытке добавления нового типа коммунальных услуг, если он  уже присутствует в справочнике
 */
public class PublicUtilityTypeAlreadyExistsException extends RuntimeException {
    public PublicUtilityTypeAlreadyExistsException(String utilityName) {
        super("Public utility " + utilityName + " already exists!");
    }
}
