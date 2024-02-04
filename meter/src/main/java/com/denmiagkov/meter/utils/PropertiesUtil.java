package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.application.exception.PropertiesFileNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

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
