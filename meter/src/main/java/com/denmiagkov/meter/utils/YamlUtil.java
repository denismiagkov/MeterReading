package com.denmiagkov.meter.utils;

import com.denmiagkov.meter.utils.exception.PropertiesFileNotFoundException;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class YamlUtil {
    public static final String YAML_FILE = "application.yml";
    private static final Yaml YAML = new Yaml();
    private static Map<String, Object> properties;

    private YamlUtil() {
    }

    static {
        try {
            loadYaml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadYaml() throws IOException {
        try (InputStream inputStream = YamlUtil.class
                .getClassLoader()
                .getResourceAsStream(YAML_FILE)) {
            properties = YAML.load(inputStream);
            Map<String, String> result = new HashMap<>();
            for (String key : properties.keySet()) {
                if (properties.get(key) != null) {
                 //   result.put(key, properties.get(key));
                }
            }
        } catch (IOException e) {
            throw new PropertiesFileNotFoundException(e.getMessage());
        }
    }

    public static String get(String key) {
        return (String) properties.get(key);
    }

}