package io.jacobking.quickticket.core.config;

import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.alert.Alerts;
import org.jooq.tools.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class Config implements ConfigDefaulter {

    private final String     path;
    private final Properties properties;

    public Config(final String path) {
        this.path = path;
        this.properties = new Properties();
        readProperties();
    }

    public String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getProperty(final String key) {
        return getProperty(key, StringUtils.EMPTY);
    }

    public Object putProperty(final String key, final String value) {
        final Object insert = properties.setProperty(key, value);
        if (insert != null) {
            storeProperties();
        }
        return insert;
    }

    public Properties getProperties() {
        return properties;
    }

    public void readProperties() {
        if (!FileIO.fileExists(path)) {
            createProperties(path);
            return;
        }

        try (final FileInputStream fileInputStream = new FileInputStream(path)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            Alerts.showException("Failed to read properties file.", e.fillInStackTrace());
        }
    }

    public void storeProperties() {
        try (final FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            Alerts.showException("Failed to store properties file.", e.fillInStackTrace());
        }
    }

    private void createProperties(final String path) {
        if (FileIO.createFile(path)) {
            putDefaults();
            storeProperties();
        }
    }
}
