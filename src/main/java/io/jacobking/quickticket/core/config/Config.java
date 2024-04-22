package io.jacobking.quickticket.core.config;

import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.core.utility.Logs;
import org.jooq.tools.StringUtils;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public abstract class Config {

    private final String     fileName;
    private final Properties properties;

    public Config(String fileName) {
        Logs.info("{} initialized.", getClass().getName());
        this.fileName = FileIO.getPath(fileName);
        System.out.println(fileName);
        this.properties = new Properties();
        configureProperties();
    }

    public abstract void putDefaultProperties();

    public void setProperty(final String key, final String value) {
        properties.setProperty(key, value);
    }

    public void setPropertyAndStore(final String key, final String value) {
        setProperty(key, value);
        storeProperties();
    }

    public String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public boolean parseBoolean(final String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public String getProperty(final String key) {
        return getProperty(key, StringUtils.EMPTY);
    }

    public Properties getProperties() {
        return properties;
    }

    private void configureProperties() {
        if (FileIO.fileExists(fileName, false)) {
            loadProperties();
            checkIfEmpty();
            return;
        }

        putDefaultProperties();
        storeProperties();
    }

    private void checkIfEmpty() {
        if (properties.isEmpty()) {
            putDefaultProperties();
            storeProperties();
        }
    }

    private void loadProperties() {
        try {
            properties.load(new FileReader(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeProperties() {
        try {
            properties.store(new FileOutputStream(fileName), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
