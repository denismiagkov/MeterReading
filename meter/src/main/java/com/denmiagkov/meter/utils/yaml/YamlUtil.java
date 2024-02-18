package com.denmiagkov.meter.utils.yaml;

import com.denmiagkov.meter.utils.exception.PropertiesFileNotFoundException;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class YamlUtil {
    private static final String YAML_FILE = "application.yaml";
    private static final Yaml YAML = new Yaml(new Constructor(YamlConfig.class, new LoaderOptions()));
    private static YamlConfig yamlConfig;

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
        try (InputStream inputStream = YamlUtil.class.getClassLoader()
                .getResourceAsStream(YAML_FILE)) {
            yamlConfig = YAML.load(inputStream);
        } catch (IOException e) {
            throw new PropertiesFileNotFoundException(e.getMessage());
        }
    }

    public static YamlConfig getYaml() {
        return yamlConfig;
    }
}