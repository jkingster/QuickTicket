package io.jacobking.quickticket.core.config;

import io.jacobking.quickticket.core.utility.FileIO;
import org.jooq.tools.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class Config implements ConfigDefaulter {

    private final Properties properties = new Properties();

    private final String filePath;

    public Config(final String path) {
        this.filePath = path;
    }

    public String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getProperty(final String key) {
        return getProperty(key, StringUtils.EMPTY);
    }

    public boolean parseBoolean(final String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    public Object putProperty(final String key, final String value) {
        final Object object = properties.setProperty(key, value);
        if (object != null) {
            storeProperties();
        }
        return object;
    }

    public void checkForConfig() {
        if (FileIO.fileExists(filePath)) {
            loadProperties();
            return;
        }
        createConfig();
    }

    private void createConfig() {
        if (FileIO.createFile(filePath)) {
            System.out.println(filePath);
            putDefaults();
            storeProperties();
        }
    }

    private void loadProperties() {
        try (final FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeProperties() {
        if (!properties.isEmpty()) {
            try (final FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                properties.store(fileOutputStream, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
