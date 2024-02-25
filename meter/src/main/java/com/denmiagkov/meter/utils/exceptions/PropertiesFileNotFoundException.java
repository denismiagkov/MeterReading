package com.denmiagkov.meter.utils.exceptions;

/**
 * Исключение, выбрасываемое в случае невозможности загрузки properties-файла
 */
public class PropertiesFileNotFoundException extends RuntimeException {
    public PropertiesFileNotFoundException(String message) {
        super(message);
    }
}
