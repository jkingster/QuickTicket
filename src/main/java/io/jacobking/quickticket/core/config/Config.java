package io.jacobking.quickticket.core.config;

import io.jacobking.quickticket.core.utility.FileIO;
import org.jooq.tools.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class Config {

    private final Properties defaultProperties = new Properties();
    private final String     filePath;
    private final Properties loadedProperties;

    public Config(final String filePath) {
        this.filePath = filePath;
        this.loadedProperties = new Properties();
        initialize();
    }

    public abstract void setDefaultProperties();

    public Properties getProperties() {
        return loadedProperties;
    }

    public void putDefaultProperty(final String key, final String value) {
        defaultProperties.setProperty(key, value);
    }

    public String getProperty(final String key) {
        return loadedProperties.getProperty(key, StringUtils.EMPTY);
    }

    public void setPropertyAndStore(final String key, final String value) {
        if (!loadedProperties.containsKey(key))
            return;
        loadedProperties.setProperty(key, value);
        storeProperties();
    }

    public boolean parseBoolean(final String key) {
        if (!loadedProperties.containsKey(key))
            return false;
        return Boolean.parseBoolean(loadedProperties.getProperty(key));
    }

    private void initialize() {
        setDefaultProperties();
        if (FileIO.fileExists(filePath, true)) {
            loadProperties(filePath);
            storeNewProperties(compareProperties(loadedProperties, defaultProperties));
            return;
        }
        handleNewPropertiesFile();
    }

    private void handleNewPropertiesFile() {
        for (final String key : defaultProperties.stringPropertyNames()) {
            loadedProperties.setProperty(key, defaultProperties.getProperty(key));
        }
        storeProperties();
    }

    private boolean compareProperties(final Properties loadedProperties, final Properties defaultProperties) {
        boolean storeNewProperties = false;
        for (final String key : defaultProperties.stringPropertyNames()) {
            if (loadedProperties.containsKey(key))
                continue;
            loadedProperties.setProperty(key, defaultProperties.getProperty(key));
            storeNewProperties = true;
        }
        return storeNewProperties;
    }

    private void storeNewProperties(final boolean hasNewProperties) {
        if (hasNewProperties) {
            storeProperties();
        }
    }

    private void loadProperties(final String filePath) {
        try {
            final FileInputStream fileInputStream = new FileInputStream(FileIO.getPath(filePath));
            loadedProperties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void storeProperties() {
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(FileIO.getPath(filePath));
            loadedProperties.store(fileOutputStream, null);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
