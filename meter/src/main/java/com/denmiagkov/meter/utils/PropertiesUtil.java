package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.application.exception.PropertiesFileNotFoundException;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    private PropertiesUtil(){
    }

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new PropertiesFileNotFoundException(e.getMessage());
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
