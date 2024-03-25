package io.jacobking.quickticket.core;

import io.jacobking.quickticket.core.utility.FileIO;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Config     config = new Config().loadProperties();
    private final        Properties properties;

    private Config() {
        this.properties = new Properties();
    }

    public static Config getInstance() {
        return config;
    }

    private Config loadProperties() {
        loadData();
        return this;
    }

    public String readProperty(final String key) {
        return properties.getProperty(key, "");
    }

    public void setProperty(final String key, final String value) {
        properties.setProperty(key, value);
        storeProperties();
    }

    private void loadData() {
        final String targetProperties = FileIO.TARGET_PROPERTIES;
        if (FileIO.fileExists(targetProperties)) {
            processProperties();
            return;
        }

        if (!FileIO.directoryExists(FileIO.TARGET_DIRECTORY)) {
            FileIO.createDirectory(FileIO.TARGET_DIRECTORY);
            createProperties(targetProperties);
        }

        if (!FileIO.directoryExists(FileIO.TARGET_BACKUP_FOLDER)) {
            FileIO.createDirectory(FileIO.TARGET_BACKUP_FOLDER);
        }
    }

    private void processProperties() {
        try {
            properties.load(new FileReader(FileIO.TARGET_PROPERTIES));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties. {}", e.getCause());
        }

        checkDatabaseLocation(readProperty("db_url"));
    }

    private void checkDatabaseLocation(final String dbUrl) {
        if (!FileIO.fileExists(dbUrl)) {
            FileIO.createFile(dbUrl);
        }
    }

    private void createProperties(final String targetPath) {
        if (!FileIO.createFile(targetPath)) {
            throw new RuntimeException("Failed to create properties file.");
        }
        loadDefaultValues();
    }

    private void loadDefaultValues() {
        if (!FileIO.createFile(FileIO.TARGET_DATABASE)) {
            throw new RuntimeException("Failed to create database file.");
        }

        properties.setProperty("db_url", FileIO.TARGET_DATABASE);
        storeProperties();
    }

    private void storeProperties() {
        try {
            properties.store(new FileWriter(FileIO.TARGET_PROPERTIES), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
