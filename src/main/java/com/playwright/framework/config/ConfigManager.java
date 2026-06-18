package com.playwright.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import com.playwright.framework.utils.LoggerUtils;
import org.slf4j.Logger;

/**
 * Provides read-only access to framework configuration loaded from the classpath.
 */
public final class ConfigManager {

    private static final Logger LOGGER = LoggerUtils.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE = "config.properties";

    private final Properties properties;

    private ConfigManager() {
        properties = loadProperties();
    }

    private static class InstanceHolder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    public static ConfigManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

public String getProperty(String key) {
    Objects.requireNonNull(key, "Configuration key must not be null");

    if (key.isBlank()) {
        throw new IllegalArgumentException("Configuration key must not be blank");
    }

    // System Property Override
    String systemValue = System.getProperty(key);
    if (systemValue != null && !systemValue.isBlank()) {
        return systemValue;
    }

    return properties.getProperty(key);
}

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    public int getInt(String key) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Invalid integer value for property: " + key,
                    exception);
        }
    }

    private Properties loadProperties() {
        Properties loadedProperties = new Properties();
        ClassLoader classLoader = ConfigManager.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException(
                        "Configuration file '" + CONFIG_FILE + "' was not found on the classpath");
            }

            loadedProperties.load(inputStream);
            LOGGER.info("Loaded framework configuration from {}", CONFIG_FILE);
            return loadedProperties;
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to load configuration file '" + CONFIG_FILE + "'", exception);
        }
    }
}
