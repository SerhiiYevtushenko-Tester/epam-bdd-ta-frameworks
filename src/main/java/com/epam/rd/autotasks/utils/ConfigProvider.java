package com.epam.rd.autotasks.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProvider {
    private static final Logger logger = LogManager.getLogger(ConfigProvider.class);

    private static volatile ConfigProvider instance;

    private Properties properties = new Properties();
    private Properties secretProperties = new Properties();

    private ConfigProvider() {
        String env = System.getProperty("env", "qa");
        String configPath = env + ".properties";

        try (InputStream envStream = getClass().getClassLoader().getResourceAsStream(configPath);
             InputStream secretStream = getClass().getClassLoader().getResourceAsStream("secret.properties")) {

            if (envStream != null) {
                properties.load(envStream);
            } else {
                throw new RuntimeException("Configuration file not found: " + configPath);
            }

            if (secretStream != null) {
                secretProperties.load(secretStream);
            } else {
                logger.warn("secret.properties not found! Make sure to create it.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration files", e);
        }
    }

    public static ConfigProvider getInstance() {
        if (instance == null) {
            synchronized (ConfigProvider.class) {
                if (instance == null) {
                    instance = new ConfigProvider();
                }
            }
        }
        return instance;
    }

    public String getProperty(String key) {
        return secretProperties.getProperty(key, properties.getProperty(key));
    }

    public String getRequiredProperty(String key) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Required configuration property is missing or empty: '" + key + "'. Fail fast!");
        }
        return value;
    }
}
