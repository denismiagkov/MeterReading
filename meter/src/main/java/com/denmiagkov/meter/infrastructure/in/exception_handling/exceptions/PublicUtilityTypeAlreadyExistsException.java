package com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions;

/**
 * Исключение, выбрасываемое при попытке добавления нового типа коммунальных услуг, если он  уже присутствует в справочнике
 */
public class PublicUtilityTypeAlreadyExistsException extends ValidationException {
    public PublicUtilityTypeAlreadyExistsException(String utilityName) {
        super("Public utility " + utilityName + " already exists!");
    }
}
